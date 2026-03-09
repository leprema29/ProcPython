import paramiko
import os
import stat
from app.metier.variables import Variables


def create_ssh_client(host, user, password):
    """Create and return an SSH client connected to the remote host"""
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    private_key_path = Variables.get_id_rsa_path()

    try:
        if os.path.exists(private_key_path):
            private_key = paramiko.RSAKey.from_private_key_file(private_key_path)
            client.connect(host, port=22, username=user, pkey=private_key)
        else:
            client.connect(host, port=22, username=user, password=password)
    except Exception:
        client.connect(host, port=22, username=user, password=password)

    return client


def execute_remote_command(host, user, password, command, timeout=600):
    """Execute a command on a remote host via SSH and return stdout"""
    client = create_ssh_client(host, user, password)
    try:
        stdin, stdout, stderr = client.exec_command(command, timeout=timeout)
        exit_status = stdout.channel.recv_exit_status()
        output = stdout.read().decode('utf-8', errors='replace')
        err_output = stderr.read().decode('utf-8', errors='replace')
        if err_output:
            print(f"STDERR: {err_output}")
        if exit_status != 0:
            print(f"Command exited with status {exit_status}")
        return output
    finally:
        client.close()


def sftp_download_file(host, user, password, remote_path, local_path):
    """Download a file from remote host via SFTP"""
    client = create_ssh_client(host, user, password)
    try:
        sftp = client.open_sftp()
        try:
            os.makedirs(os.path.dirname(local_path), exist_ok=True)
            sftp.get(remote_path, local_path)
        finally:
            sftp.close()
    finally:
        client.close()


def sftp_read_file(host, user, password, remote_path):
    """Read a remote file content via SFTP"""
    client = create_ssh_client(host, user, password)
    try:
        sftp = client.open_sftp()
        try:
            with sftp.open(remote_path, 'r') as f:
                content = f.read().decode('utf-8', errors='replace')
            return content
        except Exception:
            return ""
        finally:
            sftp.close()
    finally:
        client.close()


def sftp_recursive_delete(host, user, password, remote_path):
    """Recursively delete a remote directory via SFTP"""
    client = create_ssh_client(host, user, password)
    try:
        sftp = client.open_sftp()
        try:
            _recursive_delete(sftp, remote_path)
        except Exception as e:
            import traceback
            traceback.print_exc()
        finally:
            sftp.close()
    finally:
        client.close()


def _recursive_delete(sftp, path):
    """Helper to recursively delete files/dirs on SFTP"""
    try:
        file_list = sftp.listdir_attr(path)
    except IOError:
        return

    for item in file_list:
        full_path = path + "/" + item.filename
        if item.filename in ('.', '..'):
            continue
        if stat.S_ISDIR(item.st_mode):
            _recursive_delete(sftp, full_path)
            try:
                sftp.rmdir(full_path)
            except Exception:
                pass
        else:
            try:
                sftp.remove(full_path)
            except Exception:
                pass
    try:
        sftp.rmdir(path)
    except Exception:
        pass
