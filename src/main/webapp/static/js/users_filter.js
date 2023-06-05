function showUsersFilter() {
    if (navigator.cookieEnabled === false){
        alert("Cookies отключены!");
    }

    document.querySelector('.spinner').style.display = 'block';

    let request = new XMLHttpRequest();
    request.open('GET', '/filter/users');
    request.responseType = 'json';
    request.send();
    request.onload = function () {

        let responseObj = request.response;

        let paramsForGroup = document.getElementById("UserGroupParam").firstElementChild
        paramsForGroup.innerHTML = `<option value=""></option>`
        paramsForGroup.innerHTML += `<option value="1">Содержит</option>`
        paramsForGroup.innerHTML += `<option value="0">Не сожержит</option>`
        paramsForGroup.value = getCookie('UserGroupParam')

        let paramsForRole = document.getElementById("UserRoleParam").firstElementChild
        paramsForRole.innerHTML = `<option value=""></option>`
        paramsForRole.innerHTML += `<option value="1">Содержит</option>`
        paramsForRole.innerHTML += `<option value="0">Не сожержит</option>`
        paramsForRole.value = getCookie('UserRoleParam')

        let UserGroupValue = document.getElementById("UserGroupValue").firstElementChild
        UserGroupValue.innerHTML = `<option></option>`
        for (let i = 0; i < responseObj.groups.length; i++) {
            UserGroupValue.innerHTML += `<option value="${Object.values(responseObj.groups[i])}">${Object.keys(responseObj.groups[i])}</option>`
        }
        UserGroupValue.value = getCookie('UserGroupValue')


        let UserRoleValue = document.getElementById("UserRoleValue").firstElementChild
        UserRoleValue.innerHTML = `<option></option>`
        for (let i = 0; i < responseObj.roles.length; i++) {
            UserRoleValue.innerHTML += `<option value="${Object.values(responseObj.roles[i])}">${Object.keys(responseObj.roles[i])}</option>`
        }
        UserRoleValue.value = getCookie('UserRoleValue')

        let UserLockedValue = document.getElementById("UserLockedValue").firstElementChild
        UserLockedValue.innerHTML = `<option value=""></option>`
        UserLockedValue.innerHTML += `<option value="1">Да</option>`
        UserLockedValue.innerHTML += `<option value="0">Нет</option>`
        UserLockedValue.value = getCookie('UserLockedValue')

        document.querySelector('.filter_window').style.display = 'block';
        document.querySelector('.spinner').style.display = 'none';
    };


}

function hideUsersFilter() {
    // document.getElementById("UserGroupValue").querySelector('.value').innerHTML = ``
    // document.getElementById("UserRoleValue").querySelector('.value').innerHTML = ``
    document.querySelector('.filter_window').style.display = 'none';
}

function applyUsersFilter() {

    setCookie('UserGroupValue', document.getElementById("UserGroupValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('UserRoleValue', document.getElementById("UserRoleValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('UserLockedValue', document.getElementById("UserLockedValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('UserGroupParam', document.getElementById("UserGroupParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('UserRoleParam', document.getElementById("UserRoleParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})


    // document.getElementById("TaskStatusValue").querySelector('.value').innerHTML = ``
    // document.getElementById("TaskPriorityValue").querySelector('.value').innerHTML = ``
    document.querySelector('.filter_window').style.display = 'none';

    usersManager.buildPage()
}

function removeFilter(value, param) {
    document.getElementById(value).firstElementChild.value = ``
    setCookie(value, '', {secure: true, 'max-age': 3600})

    if (param !== '')
        document.getElementById(param).firstElementChild.value = ``
        setCookie(param, '', {secure: true, 'max-age': 3600})

}

