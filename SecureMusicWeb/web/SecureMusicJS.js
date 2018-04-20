function switchToLogin() {
    
    document.getElementById('register').style.display = 'none';
    document.getElementById('login').style.display = 'block';
    
}

function switchToRegister() {
    
    document.getElementById('login').style.display = 'none';
    document.getElementById('register').style.display = 'block';
    
}

$(document).ready(function () {
    $("#register").hide();
    $("#login").show();
    $('#passwordReminder').hide();
});

function setPassword() {
    document.getElementById('passwordReminder').style.display = 'block';
}

function hideSetPassword() {
    document.getElementById('passwordReminder').style.display = 'none';
}

//function noUserDisplay() {
//    if(document.getElementById("user").value = )
//        return true;
//    else if (document.getElementById("pass").value == verified)
//        return true;
//    else
//        document.getElementById("error").innerHTML = "Username or Password is incorrect"
//        return false;
//}