function doOrder(object, orderBy, tableColumnID) {
    if (!object.requestFinished) return;
    if (object.tableColumnIdForOrderBy === tableColumnID) {
        if (object.ascDesc === 'ASC')
            object.ascDesc = 'DESC'
        else object.ascDesc = 'ASC'
    } else object.ascDesc = 'ASC'

    object.orderBy = orderBy
    object.tableColumnIdForOrderBy = tableColumnID
    object.buildPage()
}

function addSearchEnterListener(object) {
    (function () {
        document.querySelector('.searchText').addEventListener('keydown', function (e) {
            if (e.keyCode === 13) {
                doSearch(object)
            }
        });
    })();
}

function focus() {
    document.getElementById("searchText").focus();
    document.getElementById("searchText").select();
}

function doSearch(object) {
    object.pageNumber = 1
    object.searchText = document.getElementById("searchText").value
    object.buildPage()
}

function dropDownSearch(object) {
    if (!object.dropDownSearchClicked) {
        object.dropDownSearchClicked = true
        document.getElementById("selectorParams").style.display = 'block'
        console.log('open')

    } else {
        object.dropDownSearchClicked = false
        document.getElementById("selectorParams").style.display = ''
        console.log('close')
    }
}

function setSearchParam(object, text, searchUrl) {
    object.searchUrl = searchUrl
    object.searchBy = text;
    document.getElementById("selector").textContent = text + ':'
    document.getElementById("selectorParams").style.display = 'none'
    object.dropDownSearchClicked = false
    focus()
}

function clearSearchText (object) {
    document.getElementById("searchText").value = ''
    object.searchText = ''
    focus()
}

function turnPage(object, pageNumber) {
    object.pageNumber = pageNumber
    object.buildPage()
}

function buildRequest(object, url, buttonPressed) {
    object.methodUrl = url;

    if (object.buttonPressed === '')
        object.buttonPressed = buttonPressed;
    else {
        document.getElementById(object.buttonPressed).style.backgroundColor = '#eee';
        document.getElementById(object.buttonPressed).style.boxShadow = '0 1px 0 rgba(255, 255, 255, .9) inset, 0 1px 3px rgba(0, 0, 0, .1)';
        object.buttonPressed = buttonPressed;
    }

    document.getElementById(buttonPressed).style.backgroundColor = 'rgb(210, 210, 210)';
    document.getElementById(buttonPressed).style.boxShadow = 'none';

    object.buildPage();
}