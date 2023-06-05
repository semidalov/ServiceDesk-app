function showTasksFilter() {
    if (navigator.cookieEnabled === false){
        alert("Cookies отключены!");
    }

    document.querySelector('.spinner').style.display = 'block';

    let request = new XMLHttpRequest();
    request.open('GET', '/filter/tasks');
    request.responseType = 'json';
    request.send();
    request.onload = function () {

        let responseObj = request.response;

        let paramsForGroup = document.getElementById("TaskStatusParam").firstElementChild
        paramsForGroup.innerHTML = `<option value=""></option>`
        paramsForGroup.innerHTML += `<option value="1">Содержит</option>`
        paramsForGroup.innerHTML += `<option value="0">Не сожержит</option>`
        paramsForGroup.value = getCookie('TaskStatusParam')

        let paramsForRole = document.getElementById("TaskPriorityParam").firstElementChild
        paramsForRole.innerHTML = `<option value=""></option>`
        paramsForRole.innerHTML += `<option value="1">Содержит</option>`
        paramsForRole.innerHTML += `<option value="0">Не сожержит</option>`
        paramsForRole.value = getCookie('TaskPriorityParam')

        let paramsForDate = document.getElementById("TaskDateParam").firstElementChild
        paramsForDate.innerHTML = `<option value=""></option>`
        paramsForDate.innerHTML += `<option value="equals">Равно</option>`
        paramsForDate.innerHTML += `<option value="between">Между</option>`
        paramsForDate.value = getCookie('TaskDateParam')


        let TaskStatusValue = document.getElementById("TaskStatusValue").firstElementChild
        TaskStatusValue.innerHTML = `<option></option>`
        for (let i = 0; i < responseObj.statuses.length; i++) {
            TaskStatusValue.innerHTML += `<option value="${Object.values(responseObj.statuses[i])}">${Object.keys(responseObj.statuses[i])}</option>`
        }
        TaskStatusValue.value = getCookie('TaskStatusValue')


        let TaskPriorityValue = document.getElementById("TaskPriorityValue").firstElementChild
        TaskPriorityValue.innerHTML = `<option></option>`
        for (let i = 0; i < responseObj.priorities.length; i++) {
            TaskPriorityValue.innerHTML += `<option value="${Object.values(responseObj.priorities[i])}">${Object.keys(responseObj.priorities[i])}</option>`
        }
        TaskPriorityValue.value = getCookie('TaskPriorityValue')

        document.getElementById("TaskDateValue").querySelector('.value').value = getCookie('TaskDateValue')


        document.querySelector('.filter_window').style.display = 'block';
        document.querySelector('.spinner').style.display = 'none';
    };


}

function hideTasksFilter() {
    document.getElementById("TaskStatusValue").querySelector('.value').innerHTML = ``
    document.getElementById("TaskPriorityValue").querySelector('.value').innerHTML = ``
    document.querySelector('.filter_window').style.display = 'none';
}

function applyTasksFilter() {

    setCookie('TaskStatusValue', document.getElementById("TaskStatusValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('TaskPriorityValue', document.getElementById("TaskPriorityValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})


    setCookie('TaskDateValue', document.getElementById("TaskDateValue")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('TaskStatusParam', document.getElementById("TaskStatusParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('TaskPriorityParam', document.getElementById("TaskPriorityParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})

    setCookie('TaskDateParam', document.getElementById("TaskDateParam")
        .firstElementChild.value,
        {secure: true, 'max-age': 3600})


    document.getElementById("TaskStatusValue").querySelector('.value').innerHTML = ``
    document.getElementById("TaskPriorityValue").querySelector('.value').innerHTML = ``
    document.querySelector('.filter_window').style.display = 'none';

    taskManager.buildPage()
}

function removeFilter(value, param) {
    document.getElementById(value).firstElementChild.value = ``
    document.getElementById(param).firstElementChild.value = ``

    setCookie(value, '', {secure: true, 'max-age': 3600})
    setCookie(param, '', {secure: true, 'max-age': 3600})
}

