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
});

