class EquipmentManager {
    resultsPerPage = 13;
    pageNumber = 1;
    searchBy = 'По наименованию'
    searchText = ''
    searchUrl = '&name='
    dropDownSearchClicked = false;
    requestUrl = ''

    orderBy = 'e.name'
    tableColumnIdForOrderBy = 'nameColumn'
    ascDesc = 'ASC'

    requestFinished = true;

    buildPage() {
        if (!this.requestFinished) return;
        this.requestFinished = false;

        document.querySelector('.spinner').style.display = 'block';

        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("selectorContent").innerHTML =
            `<div id="order_table" style="height: 650px; width: 1000px; margin: 0 auto; padding-top: 20px;">
                <div class="search" id="searchForm">
                    <input type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                    <input class="searchText" id="searchText" value="${equipmentManager.searchText}" placeholder="Поиск" type="search" autocomplete="off" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                    <div onclick="clearSearchText(equipmentManager)" class="resetSearchText"></div>
                    <div onclick="dropDownSearch(equipmentManager)" id="selector" class="selector">${equipmentManager.searchBy}:</div>
                    <div class="selectorParams" id="selectorParams">
                        <div onclick="setSearchParam(equipmentManager, this.textContent, '&title=')"><span style="margin-left: 10px">По наименованию</span><span style="margin-right: 10px"></span></div>
                        <div onclick="setSearchParam(equipmentManager, this.textContent, '&serial=')"><span style="margin-left: 10px"></span>По серийному<span style="margin-right: 10px"></span></div>
                    </div>
                    <button id="searchButton" onclick="doSearch(equipmentManager)" type="button"></button>  
                </div>
            </div>`
    }

    buildTable() {
        document.getElementById("order_table").innerHTML += `<table class="table" style="margin-top: 15px"></table>`
        document.querySelector('.table').style.width = `95%`
        document.querySelector('.table').innerHTML += `<thead class="thead"></thead>`
        document.querySelector('.table').innerHTML += `<tbody class="tbody" style="display: none"></tbody>`

        let thead = document.createElement('tr');
        thead.innerHTML += `<th id="nameColumn" onclick="doOrder(equipmentManager, 'e.name', this.id)">Наименование</th>`;
        thead.innerHTML += `<th id="serialColumn" onclick="doOrder(equipmentManager, 'e.serial', this.id)">Серийный номер</th>`;
        thead.innerHTML += `<th id="typeColumn" onclick="doOrder(equipmentManager, 'e.equipmentClass.id', this.id)">Тип</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (equipmentManager.ascDesc === 'ASC')
            document.getElementById(equipmentManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(equipmentManager.tableColumnIdForOrderBy).textContent += '▲'
    }

    buildUrl() {
        this.requestUrl = '/equipment/get_equipment?page=' + this.pageNumber + '&results_per_page=' + this.resultsPerPage
        if (this.orderBy !== '')
            this.requestUrl += '&order_by=' + this.orderBy + ' ' + this.ascDesc;
        if (this.searchText !== '') {
            this.requestUrl = this.requestUrl + this.searchUrl + this.searchText
        }
        console.log(this.requestUrl)
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', equipmentManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td><a onclick="fillEquipment(${responseObj[i].id}, '${responseObj[i].name}')">${responseObj[i].name}</a></td>`
                tbody.innerHTML += `<td>${responseObj[i].serial}</td>`
                tbody.innerHTML += `<td>${responseObj[i].equipmentClass}</td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            document.getElementById("select_box").style.display = 'block'
            addSearchEnterListener(equipmentManager)
            focus()
            equipmentManager.requestFinished = true;
        };
    }

    buildPagesButtons() {
        let rowsCountRequest = new XMLHttpRequest();

        if (this.searchText !== '')
            rowsCountRequest.open('GET', '/equipment/rows_count?' + this.searchUrl.substring(1) + this.searchText);
        else
            rowsCountRequest.open('GET', '/equipment/rows_count');

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {


            let pagesCount = Math.ceil(rowsCountRequest.response / equipmentManager.resultsPerPage)
            document.getElementById("selectorNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === equipmentManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(equipmentManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(equipmentManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}

class IncidentManager {
    resultsPerPage = 18;
    pageNumber = 1;
    searchBy = 'По наименованию'
    searchText = ''
    searchUrl = '&title='
    dropDownSearchClicked = false;
    requestUrl = ''
    equipmentID = ''

    orderBy = 'i.title'
    tableColumnIdForOrderBy = 'titleColumn'
    ascDesc = 'ASC'

    requestFinished = true;

    buildPage() {
        if (!this.requestFinished) return;
        this.requestFinished = false;

        if (this.equipmentID === '') {
            this.equipmentID = document.getElementById("equipment_hidden").value
        }

        document.querySelector('.spinner').style.display = 'block';
        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("selectorContent").innerHTML =
            `<div id="order_table" style="height: 650px; width: 1000px; margin: 0 auto; padding-top: 20px;">
                <div class="search" id="searchForm">
                    <input type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                    <input class="searchText" id="searchText" value="${incidentManager.searchText}" placeholder="Поиск" type="search" autocomplete="off" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                    <div onclick="clearSearchText(incidentManager)" class="resetSearchText"></div>
                    <div onclick="dropDownSearch(incidentManager)" id="selector" class="selector">${incidentManager.searchBy}:</div>
                    <div class="selectorParams" id="selectorParams">
                        <div onclick="setSearchParam(incidentManager, this.textContent, '&title=')"><span style="margin-left: 10px">По наименованию</span><span style="margin-right: 10px"></span></div>
                    </div>
                    <button id="searchButton" onclick="doSearch(incidentManager)" type="button"></button>  
                </div>
            </div>`

    }

    buildTable() {
        document.getElementById("order_table").innerHTML += `<table class="table" style="margin-top: 15px"></table>`
        document.querySelector('.table').style.width = `95%`
        document.querySelector('.table').innerHTML += `<thead class="thead"></thead>`
        document.querySelector('.table').innerHTML += `<tbody class="tbody" style="display: none"></tbody>`

        let thead = document.createElement('tr');
        thead.innerHTML += `<th id="titleColumn" onclick="doOrder(incidentManager, 'i.title', this.id)" >Наименование</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (incidentManager.ascDesc === 'ASC')
            document.getElementById(incidentManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(incidentManager.tableColumnIdForOrderBy).textContent += '▲'
    }

    buildUrl() {

        this.requestUrl = '/incidents/incidents?equipment_id=' +
            this.equipmentID + '&page=' + this.pageNumber + '&results_per_page=' + this.resultsPerPage
        if (this.orderBy !== '')
            this.requestUrl += '&order_by=' + this.orderBy + ' ' + this.ascDesc;
        if (this.searchText !== '') {
            this.requestUrl = this.requestUrl + this.searchUrl + this.searchText
        }
        console.log(this.requestUrl)
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', incidentManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td><a onclick="fillIncident(${responseObj[i].id}, '${responseObj[i].title}')">${responseObj[i].title}</a></td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            document.getElementById("select_box").style.display = 'block'
            addSearchEnterListener(incidentManager)
            focus()
            incidentManager.requestFinished = true;
        };
    }

    buildPagesButtons() {
        let rowsCountRequest = new XMLHttpRequest();

        if (this.searchText !== '')
            rowsCountRequest.open('GET', '/incidents/rows_count/'+ this.equipmentID + '?' + this.searchUrl.substring(1) + this.searchText);
        else
            rowsCountRequest.open('GET', '/incidents/rows_count/' + this.equipmentID);

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {

            let pagesCount = Math.ceil(rowsCountRequest.response / incidentManager.resultsPerPage)
            document.getElementById("selectorNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === incidentManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(incidentManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons"  onclick="turnPage(incidentManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}

class ExecutorManager {
    resultsPerPage = 18;
    pageNumber = 1;
    searchBy = 'По имени'
    searchText = ''
    searchUrl = '&last_name='
    dropDownSearchClicked = false;
    requestUrl = ''

    orderBy = 'e.lastName'
    tableColumnIdForOrderBy = 'nameColumn'
    ascDesc = 'ASC'

    requestFinished = true;

    buildPage() {
        if (!this.requestFinished) return;
        this.requestFinished = false;
        document.querySelector('.spinner').style.display = 'block';

        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("selectorContent").innerHTML =
            `<div id="order_table" style="height: 650px; width: 1000px; margin: 0 auto; padding-top: 20px;">
                <div class="search" id="searchForm">
                    <input type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                    <input class="searchText" id="searchText" value="${executorManager.searchText}" placeholder="Поиск" type="search" autocomplete="off" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                    <div onclick="clearSearchText(executorManager)" class="resetSearchText"></div>
                    <div onclick="dropDownSearch(executorManager)" id="selector" class="selector">${executorManager.searchBy}:</div>
                    <div class="selectorParams" id="selectorParams">
                        <div onclick="setSearchParam(executorManager, this.textContent, '&last_name=')"><span style="margin-left: 10px">По имени</span><span style="margin-right: 10px"></span></div>
                    </div>
                    <button id="searchButton" onclick="doSearch(executorManager)" type="button"></button>  
                </div>
            </div>`
    }

    buildTable() {
        document.getElementById("order_table").innerHTML += `<table class="table" style="margin-top: 15px"></table>`
        document.querySelector('.table').style.width = `95%`
        document.querySelector('.table').innerHTML += `<thead class="thead"></thead>`
        document.querySelector('.table').innerHTML += `<tbody class="tbody" style="display: none"></tbody>`

        let thead = document.createElement('tr');
        thead.innerHTML += `<th id="nameColumn" onclick="doOrder(executorManager, 'e.lastName', this.id)">Имя</th>`;
        thead.innerHTML += `<th id="groupColumn" onclick="doOrder(executorManager, 'e.group.id', this.id)">Отдел</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (executorManager.ascDesc === 'ASC')
            document.getElementById(executorManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(executorManager.tableColumnIdForOrderBy).textContent += '▲'
    }

    buildUrl() {
        this.requestUrl = '/users/get_executors?page=' + this.pageNumber + '&results_per_page=' + this.resultsPerPage
        if (this.orderBy !== '')
            this.requestUrl += '&order_by=' + this.orderBy + ' ' + this.ascDesc;
        if (this.searchText !== '') {
            this.requestUrl = this.requestUrl + this.searchUrl + this.searchText
        }
        console.log(this.requestUrl)
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', executorManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td><a onclick="fillExecutor(${responseObj[i].id}, '${responseObj[i].fullName}')">${responseObj[i].fullName}</a></td>`
                tbody.innerHTML += `<td>${responseObj[i].groupName}</td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            document.getElementById("select_box").style.display = 'block'
            addSearchEnterListener(executorManager)
            focus()
            executorManager.requestFinished = true;
        };

    }

    buildPagesButtons() {
        let rowsCountRequest = new XMLHttpRequest();

        let url;
        if (this.searchText !== '')
            url ='/users/rows_count?role=3' + this.searchUrl + this.searchText
        else
            url = '/users/rows_count?role=3'

        console.log(url)
        rowsCountRequest.open('GET', url);

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {


            let pagesCount = Math.ceil(rowsCountRequest.response / executorManager.resultsPerPage)
            document.getElementById("selectorNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === executorManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(executorManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(executorManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}

class OccasionManager {
    resultsPerPage = 18;
    pageNumber = 1;
    searchBy = 'По наименованию'
    searchText = ''
    searchUrl = '&name='
    dropDownSearchClicked = false;
    requestUrl = ''

    orderBy = 'o.name'
    tableColumnIdForOrderBy = 'nameColumn'
    ascDesc = 'ASC'

    requestFinished = true;

    buildPage() {
        if (!this.requestFinished) return;
        this.requestFinished = false;

        document.querySelector('.spinner').style.display = 'block';

        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("selectorContent").innerHTML =
            `<div id="order_table" style="height: 650px; width: 1000px; margin: 0 auto; padding-top: 20px;">
                <div class="search" id="searchForm">
                    <input type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                    <input class="searchText" id="searchText" value="${occasionManager.searchText}" placeholder="Поиск" type="search" autocomplete="off" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                    <div onclick="clearSearchText(occasionManager)" class="resetSearchText"></div>
                    <div onclick="dropDownSearch(occasionManager)" id="selector" class="selector">${occasionManager.searchBy}:</div>
                    <div class="selectorParams" id="selectorParams">
                        <div onclick="setSearchParam(occasionManager, this.textContent, '&name=')"><span style="margin-left: 10px">По наименованию</span><span style="margin-right: 10px"></span></div>
                    </div>
                    <button id="searchButton" onclick="doSearch(occasionManager)" type="button"></button>  
                </div>
            </div>`
    }

    buildTable() {
        document.getElementById("order_table").innerHTML += `<table class="table" style="margin-top: 15px"></table>`
        document.querySelector('.table').style.width = `95%`
        document.querySelector('.table').innerHTML += `<thead class="thead"></thead>`
        document.querySelector('.table').innerHTML += `<tbody class="tbody" style="display: none"></tbody>`

        let thead = document.createElement('tr');
        thead.innerHTML += `<th id="nameColumn" onclick="doOrder(occasionManager, 'o.name', this.id)">Наименование</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (occasionManager.ascDesc === 'ASC')
            document.getElementById(occasionManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(occasionManager.tableColumnIdForOrderBy).textContent += '▲'
    }

    buildUrl() {
        this.requestUrl = '/occasions/get_occasions?page=' + this.pageNumber + '&results_per_page=' + this.resultsPerPage
        if (this.orderBy !== '')
            this.requestUrl += '&order_by=' + this.orderBy + ' ' + this.ascDesc;
        if (this.searchText !== '') {
            this.requestUrl = this.requestUrl + this.searchUrl + this.searchText
        }
        console.log(this.requestUrl)
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', occasionManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td><a onclick="fillOccasion(${responseObj[i].id}, '${responseObj[i].name}')">${responseObj[i].name}</a></td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            document.getElementById("select_box").style.display = 'block'
            addSearchEnterListener(occasionManager)
            focus()
            occasionManager.requestFinished = true;
        };

    }

    buildPagesButtons() {
        let rowsCountRequest = new XMLHttpRequest();

        if (this.searchText !== '')
            rowsCountRequest.open('GET', '/occasions/rows_count?' + this.searchUrl.substring(1) + this.searchText);
        else
            rowsCountRequest.open('GET', '/occasions/rows_count');

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {

            let pagesCount = Math.ceil(rowsCountRequest.response / occasionManager.resultsPerPage)
            document.getElementById("selectorNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === occasionManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(occasionManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(occasionManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}


function closeManagerWindow() {
    document.getElementById("select_box").style.display = 'none';
}


function fillExecutor(id, name) {
    document.getElementById("select_box").style.display = 'none';
    document.getElementById("executor_hidden").value = id;
    document.getElementById("executor").value = name;
}

function fillOccasion(id, name) {
    document.getElementById("select_box").style.display = 'none';
    document.getElementById("occasion_hidden").value = id;
    document.getElementById("occasion").value = name;
}

function fillEquipment(id, name) {
    document.getElementById("select_box").style.display = 'none';
    document.getElementById("equipment_hidden").value = id;
    incidentManager.equipmentID = id
    document.getElementById("equipment").value = name;


    document.getElementById("incident_hidden").value = '';
    document.getElementById("incident").value = '';
}

function fillIncident(id, title) {
    document.getElementById("select_box").style.display = 'none';
    document.getElementById("incident_hidden").value = id;
    document.getElementById("incident").value = title;
}

