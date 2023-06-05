let menuToggle = true;

function changePassword() {
    window.location.replace("/account/change_pass");
}

function showUserMenu() {
    let menu = document.getElementById("userMenu");

    if (menuToggle) {
        menu.style.opacity = '1'
        menuToggle = false
    } else {
        menu.style.opacity = '0'
        menuToggle = true
    }
}