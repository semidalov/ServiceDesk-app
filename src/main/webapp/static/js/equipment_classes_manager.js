class EquipmentClassesManager {
    buttonPressed = '';

    resultsPerPage = 16;
    pageNumber = 1;
    searchBy = 'По наименованию'
    searchText = ''
    searchUrl = '&name='
    dropDownSearchClicked = false;
    requestUrl = ''
    methodUrl = ''

    orderBy = 'c.name'
    tableColumnIdForOrderBy = 'nameColumn'
    ascDesc = 'ASC'

    requestFinished = true

    buildPage() {
        if (!this.requestFinished) return
        this.requestFinished = false
        document.querySelector('.spinner').style.display = 'block';

        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        // this.finishUrlBasedOnCookie()
        console.log(this.requestUrl)
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("resultContent"). innerHTML =
            `<div class="search" id="searchForm" style="margin-top: 10px">
                <input class="searchText1" id="searchText1" type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                <input class="searchText" id="searchText" placeholder="Поиск" type="search" autocomplete="off" value="${equipmentClassesManager.searchText}" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                <div onclick="clearSearchText(equipmentClassesManager)" class="resetSearchText"></div>
                <div onclick="dropDownSearch(equipmentClassesManager)" id="selector" class="selector">${this.searchBy}:</div>
                <div class="selectorParams" id="selectorParams"></div>
                <button id="searchButton" onclick="doSearch(equipmentClassesManager)" type="button"></button>
            </div>`

        let selector = document.getElementById("selectorParams")
        selector.innerHTML += `<div onclick="setSearchParam(equipmentClassesManager, this.textContent, '&name=')"><span style="margin-left: 10px"></span>По наименованию<span style="margin-right: 10px"></span></div>`
        document.getElementById("searchForm").appendChild(selector);

        let style = document.createElement('style')
        style.innerHTML = `@media only screen and (max-width: 2000px){
                                .table {
                                    width: 50%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                                 .search {
                                    position: relative;
                                    width: 49.9%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                            }`;
        document.body.appendChild(style)
    }

    buildTable() {
        document.getElementById("resultContent").innerHTML += `<div id="order_table" style="height: 800px; padding-top: 10px"></div>`
        document.getElementById("order_table").innerHTML += `<table class="table"></table>`
        document.querySelector('.table').innerHTML += `<thead class="thead"></thead>`
        document.querySelector('.table').innerHTML += `<tbody class="tbody"></tbody>`

        let thead = document.createElement('tr');
        thead.innerHTML += `<th id="nameColumn"  onclick="doOrder(equipmentClassesManager, 'c.name', this.id)" style="width: 30%;">Наименование</th>`;
        thead.innerHTML += `<th id="descriptionColumn"  onclick="doOrder(equipmentClassesManager, 'c.description', this.id)" style="width: 70%;">Описание</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (equipmentClassesManager.ascDesc === 'ASC')
            document.getElementById(equipmentClassesManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(equipmentClassesManager.tableColumnIdForOrderBy).textContent += '▲'
    }

    buildUrl() {
        this.requestUrl = '' + this.methodUrl + '?page=' + this.pageNumber + '&results_per_page=' + this.resultsPerPage
        if (this.orderBy !== '')
            this.requestUrl += '&order_by=' + this.orderBy + ' ' + this.ascDesc;
        if (this.searchText !== '') {
            this.requestUrl = this.requestUrl + this.searchUrl + this.searchText
        }
    }

    finishUrlBasedOnCookie() {
        if (getCookie('EquipmentClassParam')) {
            if (getCookie('EquipmentClassValue')) {
                if (getCookie('EquipmentClassParam') === '1')
                    this.requestUrl += '&equipment_class=' + getCookie('EquipmentClassValue')
                if (getCookie('EquipmentClassParam') === "0")
                    this.requestUrl += '&equipment_class=' + '!' + getCookie('EquipmentClassValue')
                if (getCookie('EquipmentClassParam') === "")
                    setCookie('EquipmentClassValue', '')
            } else
                setCookie('EquipmentClassValue', '')
        } else {
            setCookie('EquipmentClassParam', '')
            setCookie('EquipmentClassValue', '')
        }
    }



    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', equipmentClassesManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td style="text-align: left"><a href="/classes/${responseObj[i].id}">${responseObj[i].name}</a></td>`
                tbody.innerHTML += `<td>${responseObj[i].description}</td>`
                // if (responseObj[i].lastLogin === null) tbody.innerHTML += `<td></td>`
                // else tbody.innerHTML += `<td>${responseObj[i].lastLogin.replace("T", "&nbsp;&nbsp;&nbsp;")}</td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            addSearchEnterListener(equipmentClassesManager)
            focus()
            equipmentClassesManager.requestFinished = true
        };

    }

    buildPagesButtons() {
        let url = '/classes/rows_count?';
        if (this.searchText !== '')
            url += '&' + this.searchUrl.substring(1) + this.searchText;

        // if (getCookie('EquipmentClassParam') === '1')
        //     url += '&equipment_class=' + getCookie('EquipmentClassValue')
        // if (getCookie('EquipmentClassParam') === "0")
        //     url += '&equipment_class=' + '!' + getCookie('EquipmentClassValue')

        let rowsCountRequest = new XMLHttpRequest();
        rowsCountRequest.open('GET', url);

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {

            let pagesCount = Math.ceil(rowsCountRequest.response / equipmentClassesManager.resultsPerPage)
            document.getElementById("resultNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === equipmentClassesManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(equipmentClassesManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(equipmentClassesManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}


