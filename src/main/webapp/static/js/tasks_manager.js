class TasksManager {
    buttonPressed = '';

    resultsPerPage = 22;
    pageNumber = 1;
    searchBy = 'По наименованию'
    searchText = ''
    searchUrl = '&title='
    dropDownSearchClicked = false;
    requestUrl = ''
    methodUrl = ''

    orderBy = 't.taskStatus.id'
    tableColumnIdForOrderBy = 'statusColumn'
    ascDesc = 'ASC'

    requestFinished = true

    buildPage() {
        if (!this.requestFinished) return
        this.requestFinished = false
        document.querySelector('.spinner').style.display = 'block';

        this.buildSearch()
        this.buildTable()
        this.buildUrl()
        this.finishUrlBasedOnCookie()
        console.log(this.requestUrl)
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("resultContent"). innerHTML =
            `<div class="search" id="searchForm" style="margin-top: 10px">
                <input class="searchText1" id="searchText1" type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                <input class="searchText" id="searchText" placeholder="Поиск" type="search" autocomplete="off" value="${taskManager.searchText}" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                <div onclick="clearSearchText(taskManager)" class="resetSearchText"></div>
                <div onclick="dropDownSearch(taskManager)" id="selector" class="selector">${this.searchBy}:</div>
                <div class="selectorParams" id="selectorParams"></div>
                <button id="searchButton" onclick="doSearch(taskManager)" type="button"></button>
            </div>`

        let selector = document.getElementById("selectorParams")
        selector.innerHTML += `<div onclick="setSearchParam(taskManager, this.textContent, '&title=')"><span style="margin-left: 10px"></span>По наименованию<span style="margin-right: 10px"></span></div>`
        selector.innerHTML += `<div onclick="setSearchParam(taskManager, this.textContent, '&creator=')"><span style="margin-left: 10px"></span>По заявителю<span style="margin-right: 10px"></span></div>`
        selector.innerHTML += `<div onclick="setSearchParam(taskManager, this.textContent, '&executor=')"><span style="margin-left: 10px"></span>По исполнителю<span style="margin-right: 10px"></span></div>`
        document.getElementById("searchForm").appendChild(selector);

        let style = document.createElement('style')
        style.innerHTML = `@media only screen and (max-width: 2500px){
                                .table {
                                    width: 80%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                                 .search {
                                    position: relative;
                                    width: 79.9%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                            }
                            @media only screen and (max-width: 2000px){
                                .table {
                                    width: 80%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                                 .search {
                                    position: relative;
                                    width: 79.8%;
                                    margin: 0 auto;
                                    outline: none;
                                }
                            }
                            @media only screen and (max-width: 1600px){
                                .table {
                                    width: 90%;
                                }
                                  .search {
                                    width: 89.8%;
                                }
                            }
                            @media only screen and (max-width: 1400px){
                                .table {
                                    width: 100%;
                                }
                                .search {
                                    width: 99.8%;
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
        thead.innerHTML += `<th id="statusColumn"  onclick="doOrder(taskManager, 't.taskStatus.id', this.id)" style="width: 9%;">Статус</th>`;
        thead.innerHTML += `<th id="titleColumn"  onclick="doOrder(taskManager, 't.title', this.id)" style="width: 25%;">Наименование</th>`;
        thead.innerHTML += `<th id="creatorColumn"  onclick="doOrder(taskManager, 't.creator.lastName', this.id)" style="width: 15%;">Заявитель</th>`;
        thead.innerHTML += `<th id="priorityColumn"  onclick="doOrder(taskManager, 't.incident.priority.id', this.id)" style="width: 10%;">Приоритет</th>`;
        thead.innerHTML += `<th id="deadlineColumn"  onclick="doOrder(taskManager, '', this.id)" style="width: 13%;">Срок</th>`;
        thead.innerHTML += `<th id="executorColumn"  onclick="doOrder(taskManager, 't.executor.lastName', this.id)" style="width: 15%;">Исполнитель</th>`;
        thead.innerHTML += `<th id="createdColumn"  onclick="doOrder(taskManager, 't.created', this.id)" style="width: 13%;">Создано</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (taskManager.ascDesc === 'ASC')
            document.getElementById(taskManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(taskManager.tableColumnIdForOrderBy).textContent += '▲'
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
        if (getCookie('TaskStatusParam')) {
            if (getCookie('TaskStatusValue')) {
                if (getCookie('TaskStatusParam') === '1')
                    this.requestUrl += '&status=' + getCookie('TaskStatusValue')
                if (getCookie('TaskStatusParam') === "0")
                    this.requestUrl += '&status=' + '!' + getCookie('TaskStatusValue')
                if (getCookie('TaskStatusParam') === "") {
                    setCookie('TaskStatusValue', '')
                }
            } else
                setCookie('TaskStatusValue', '')
        } else {
            setCookie('TaskStatusParam', '')
            setCookie('TaskStatusValue', '')
        }

        if (getCookie('TaskPriorityParam')) {
            if (getCookie('TaskPriorityValue')) {
                if (getCookie('TaskPriorityParam') === '1')
                    this.requestUrl += '&priority=' + getCookie('TaskPriorityValue')
                if (getCookie('TaskPriorityParam') === "0")
                    this.requestUrl += '&priority=' + '!' + getCookie('TaskPriorityValue')
                if (getCookie('TaskPriorityParam') === "") {
                    setCookie('TaskPriorityValue', '')
                }
            } else
                setCookie('TaskPriorityValue', '')
        } else {
            setCookie('TaskPriorityParam', '')
            setCookie('TaskPriorityValue', '')
        }

        if (getCookie('TaskDateParam')) {
            if (getCookie('TaskDateValue')) {
                if (getCookie('TaskDateParam') === 'equals')
                    this.requestUrl += '&date=' + getCookie('TaskDateValue')
                if (getCookie('TaskDateParam') === "between")
                    this.requestUrl += '&date=' + '!' + getCookie('TaskDateValue')
                if (getCookie('TaskDateParam') === "") {
                    setCookie('TaskDateValue', '')
                }
            } else
                setCookie('TaskDateValue', '')
        } else {
            setCookie('TaskDateParam', '')
            setCookie('TaskDateValue', '')
        }
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', taskManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td>${responseObj[i].status}</td>`
                tbody.innerHTML += `<td style="text-align: left"><a onclick="document.querySelector('.spinner').style.display = 'block'" href="/tasks/${responseObj[i].id}">${responseObj[i].title}</a></td>`
                tbody.innerHTML += `<td>${responseObj[i].creator}</td>`
                tbody.innerHTML += `<td>${responseObj[i].priority}</td>`
                if (responseObj[i].expired === false)
                    tbody.innerHTML += `<td>${responseObj[i].deadline.replace("T", "&nbsp;&nbsp;&nbsp;")}</td>`
                else
                    tbody.innerHTML += `<td style="color: red">${responseObj[i].deadline.replace("T", "&nbsp;&nbsp;&nbsp;")} просрочено</td>`
                if (responseObj[i].executor === null) tbody.innerHTML += `<td></td>`
                else  tbody.innerHTML += `<td>${responseObj[i].executor}</td>`
                tbody.innerHTML += `<td>${responseObj[i].created.replace("T", "&nbsp;&nbsp;&nbsp;")}</td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            addSearchEnterListener(taskManager)
            focus()
            taskManager.requestFinished = true
        };

    }

    buildPagesButtons() {
        let url = '/tasks/rows_count?';
        if (this.searchText !== '')
            url += '&' + this.searchUrl.substring(1) + this.searchText;

        if (getCookie('TaskStatusParam') === '1')
            this.requestUrl += '&status=' + getCookie('TaskStatusValue')
        if (getCookie('TaskStatusParam') === "0")
            this.requestUrl += '&status=' + '!' + getCookie('TaskStatusValue')
        if (getCookie('TaskPriorityParam') === '1')
            this.requestUrl += '&priority=' + getCookie('TaskPriorityValue')
        if (getCookie('TaskPriorityParam') === "0")
            this.requestUrl += '&priority=' + '!' + getCookie('TaskPriorityValue')
        if (getCookie('TaskDateParam') === 'equals')
            this.requestUrl += '&date=' + getCookie('TaskDateValue')
        if (getCookie('TaskDateParam') === "between")
            this.requestUrl += '&date=' + '!' + getCookie('TaskDateValue')

        console.log(url)

        let rowsCountRequest = new XMLHttpRequest();
        rowsCountRequest.open('GET', url);

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {

            let pagesCount = Math.ceil(rowsCountRequest.response / taskManager.resultsPerPage)
            document.getElementById("resultNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === taskManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(taskManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(taskManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }

}


