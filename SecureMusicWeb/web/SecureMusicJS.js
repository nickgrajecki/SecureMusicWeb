//Brings up login options when clicking 'login'
function switchToLogin() {

    document.getElementById('register').style.display = 'none';
    document.getElementById('login').style.display = 'block';
    document.getElementById('log').style.cssText = 'background-color: white';
    document.getElementById('reg').style.cssText = 'background-color: dark-gray';

}

//Brings up register options when clicking 'register'
function switchToRegister() {

    document.getElementById('login').style.display = 'none';
    document.getElementById('register').style.display = 'block';
    document.getElementById('reg').style.cssText = 'background-color: white';
    document.getElementById('log').style.cssText = 'background-color: dark-gray';

}

//How the webpage appears upon load in
$(document).ready(function () {
    $("#register").hide();
    $("#login").show();
    $('#passwordReminder').hide();
});

//shows password requirements upon clicking 'password' input
function setPassword() {
    document.getElementById('passwordReminder').style.display = 'block';
}

//hides password requirements upon clicking elsewhere
function hideSetPassword() {
    document.getElementById('passwordReminder').style.display = 'none';
}
