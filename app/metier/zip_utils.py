import os
import zipfile


class ZipUtils:
    def __init__(self, source_folder, output_zip_file):
        self.file_list = []
        self.source_folder = source_folder
        self.output_zip_file = output_zip_file

    def generate_file_list(self, node):
        if os.path.isfile(node):
            self.file_list.append(self._generate_zip_entry(node))
        elif os.path.isdir(node):
            for filename in os.listdir(node):
                self.generate_file_list(os.path.join(node, filename))

    def zip_it(self, zip_file):
        source_name = os.path.basename(self.source_folder)
        try:
            with zipfile.ZipFile(zip_file, 'w', zipfile.ZIP_DEFLATED) as zf:
                print(f"Output to Zip : {zip_file}")
                for file in self.file_list:
                    full_path = os.path.join(self.source_folder, file)
                    arc_name = os.path.join(source_name, file)
                    print(f"File Added : {file}")
                    zf.write(full_path, arc_name)
            print("Folder successfully compressed")
        except Exception as e:
            import traceback
            traceback.print_exc()

    def _generate_zip_entry(self, file_path):
        return file_path[len(self.source_folder) + 1:]
