function showEquipmentFilter() {
    if (navigator.cookieEnabled === false){
        alert("Cookies отключены!");
    }

    document.querySelector('.spinner').style.display = 'block';

    let request = new XMLHttpRequest();
    request.open('GET', '/filter/equipment');
    request.responseType = 'json';
    request.send();
    request.onload = function () {

        let responseObj = request.response;

        let EquipmentClassParam = document.getElementById("EquipmentClassParam").firstElementChild
        EquipmentClassParam.innerHTML = `<option value=""></option>`
        EquipmentClassParam.innerHTML += `<option value="1">Содержит</option>`
        EquipmentClassParam.innerHTML += `<option value="0">Не сожержит</option>`
        EquipmentClassParam.value = getCookie('EquipmentClassParam')


        let EquipmentClassValue = document.getElementById("EquipmentClassValue").firstElementChild
        EquipmentClassValue.innerHTML = `<option></option>`
        for (let i = 0; i < responseObj.equipmentClasses.length; i++) {
            EquipmentClassValue.innerHTML += `<option value="${Object.values(responseObj.equipmentClasses[i])}">${Object.keys(responseObj.equipmentClasses[i])}</option>`
        }
        EquipmentClassValue.value = getCookie('EquipmentClassValue')

        document.querySelector('.filter_window').style.display = 'block';
        document.querySelector('.spinner').style.display = 'none';
    };


}

function hideEquipmentFilter() {
    document.querySelector('.filter_window').style.display = 'none';
}

function applyEquipmentFilter() {

    setCookie('EquipmentClassValue', document.getElementById("EquipmentClassValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})


    setCookie('EquipmentClassParam', document.getElementById("EquipmentClassParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})



    document.querySelector('.filter_window').style.display = 'none';

    equipmentManager.buildPage()
}

function removeFilter(value, param) {
    document.getElementById(value).firstElementChild.value = ``
    setCookie(value, '', {secure: true, 'max-age': 3600})

    if (param !== '')
        document.getElementById(param).firstElementChild.value = ``
        setCookie(param, '', {secure: true, 'max-age': 3600})

}

