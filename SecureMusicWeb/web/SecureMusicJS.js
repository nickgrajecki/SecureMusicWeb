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

//function scrollFade() {
//    $(window).scroll(function () {
//        $('slideshowcontainer').css("opacity", 1) - $(window).scrollTop() / 250;
//    });
//}

function scrollFade() {
    var fadeBG = $('.headerBackground');

    $(window).on('scroll', function () {
        var st = $(this).scrollTop();
        fadeBG.css({
            'opacity': 1 - st / 600
        });
    });
}

var slideIndex = 1;
showSlides(slideIndex);

// Next/previous controls
function plusSlides(n) {
    showSlides(slideIndex += n);
}

// Thumbnail image controls
function currentSlide(n) {
    showSlides(slideIndex = n);
}

function showSlides() {
    var i;
    var slides = document.getElementsByClassName("newsFade");

    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }

    slideIndex++;
    if (slideIndex > slides.length) {
        slideIndex = 1
    }
    slides[slideIndex - 1].style.display = "block";
    setTimeout(showSlides, 5000);
}

function popupPass() {
    var pop = document.getElementById("popupReminder");
    pop.classList.toggle("show");
}