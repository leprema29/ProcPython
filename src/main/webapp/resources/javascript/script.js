/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function raz1() {
//    document.forms['formulaire'].elements['nomsdomaine'].value = "";
//    document.forms['formulaire'].elements['nombreannees'].value = 0;
//    alert('Wanki');
    document.getElementById("domaine").innerHTML += '<input  value="Nom6 de domaine:" for="3nomsdomaine" />';
    return false;

}

function raz2() {
//    document.forms['formulaire'].elements['nomsserveurs'].value = "";
//    document.forms['formulaire'].elements['adresseip'].value = 0;
    document.getElementById("stay").submit();
    return false;
}

function razAll() {
    document.forms['formulaire'].elements.value = "";
}

function visible() {
//    document.getElementById('loginBox').style.display = 'block';
    var element = document.getElementById('loginBox');
    if (element.style.display === 'block') {
        element.style.display = 'none';
    } else {
        element.style.display = 'block';
    }
    return null;
}

function unvisible() {
    document.getElementById('loginBox').style.display = 'none';
}

function searchFormulaire(ok) {
    var element = document.getElementById('info');
    if (ok === 'yes') {
        element.style.display = 'none';
    } else {
        element.style.display = 'none';
    }
    return null;
}

function visible2() {
//    document.getElementById('loginBox').style.display = 'block';
    var element = document.getElementById('info');
    element.style.display = 'none';

    return null;
}