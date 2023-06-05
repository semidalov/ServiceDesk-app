class UsersManager {
    buttonPressed = '';

    resultsPerPage = 16;
    pageNumber = 1;
    searchBy = 'По имени'
    searchText = ''
    searchUrl = '&last_name='
    dropDownSearchClicked = false;
    requestUrl = ''
    methodUrl = ''

    orderBy = 'e.lastName'
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
        this.finishUrlBasedOnCookie()
        console.log(this.requestUrl)
        this.buildResponse()
        this.buildPagesButtons();
    }

    buildSearch() {
        document.getElementById("resultContent"). innerHTML =
            `<div class="search" id="searchForm" style="margin-top: 10px">
                <input class="searchText1" id="searchText1" type="search" style="pointer-events: none; width: 30px; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0">
                <input class="searchText" id="searchText" placeholder="Поиск" type="search" autocomplete="off" value="${usersManager.searchText}" style="margin-left: -10px; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0">
                <div onclick="clearSearchText(usersManager)" class="resetSearchText"></div>
                <div onclick="dropDownSearch(usersManager)" id="selector" class="selector">${this.searchBy}:</div>
                <div class="selectorParams" id="selectorParams"></div>
                <button id="searchButton" onclick="doSearch(usersManager)" type="button"></button>
            </div>`

        let selector = document.getElementById("selectorParams")
        selector.innerHTML += `<div onclick="setSearchParam(usersManager, this.textContent, '&last_name=')"><span style="margin-left: 10px"></span>По имени<span style="margin-right: 10px"></span></div>`
        selector.innerHTML += `<div onclick="setSearchParam(usersManager, this.textContent, '&work_phone=')"><span style="margin-left: 10px"></span>По телефону<span style="margin-right: 10px"></span></div>`
        selector.innerHTML += `<div onclick="setSearchParam(usersManager, this.textContent, '&username=')"><span style="margin-left: 10px"></span>По пользователю<span style="margin-right: 10px"></span></div>`
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
        thead.innerHTML += `<th id="nameColumn"  onclick="doOrder(usersManager, 'e.lastName', this.id)" style="width: 20%;">Имя</th>`;
        thead.innerHTML += `<th id="numberColumn"  onclick="doOrder(usersManager, 'e.workPhone', this.id)" style="width: 8%;">Внутренний номер</th>`;
        thead.innerHTML += `<th id="groupColumn"  onclick="doOrder(usersManager, 'e.group', this.id)" style="width: 16%;">Подразделение</th>`;
        thead.innerHTML += `<th id="usernameColumn"  onclick="doOrder(usersManager, 'u.username', this.id)" style="width: 8%;">Username</th>`;
        thead.innerHTML += `<th id="roleColumn"  onclick="doOrder(usersManager, 'u.role', this.id)" style="width: 10%;">Role</th>`;
        thead.innerHTML += `<th id="isLockedColumn"  onclick="doOrder(usersManager, 'u.locked', this.id)" style="width: 10%;">Заблокирован</th>`;
        thead.innerHTML += `<th id="createdColumn"  onclick="doOrder(usersManager, 'u.joinDate', this.id)" style="width: 15%;">Добавлен</th>`;
        thead.innerHTML += `<th id="lastLoginColumn"  onclick="doOrder(usersManager, 'u.lastLogin', this.id)" style="width: 13%;">Последний вход</th>`;
        document.querySelector('.thead').appendChild(thead);

        if (usersManager.ascDesc === 'ASC')
            document.getElementById(usersManager.tableColumnIdForOrderBy).textContent += '▼'
        else
            document.getElementById(usersManager.tableColumnIdForOrderBy).textContent += '▲'
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
        if (getCookie('UserGroupParam')) {
            if (getCookie('UserGroupValue')) {
                if (getCookie('UserGroupParam') === '1')
                    this.requestUrl += '&group=' + getCookie('UserGroupValue')
                if (getCookie('UserGroupParam') === "0")
                    this.requestUrl += '&group=' + '!' + getCookie('UserGroupValue')
                if (getCookie('UserGroupParam') === "")
                    setCookie('UserGroupValue', '')
            } else
                setCookie('UserGroupValue', '')
        } else {
            setCookie('UserGroupParam', '')
            setCookie('UserGroupValue', '')
        }

        if (getCookie('UserRoleParam')) {
            if (getCookie('UserRoleValue')) {
                if (getCookie('UserRoleParam') === '1')
                    this.requestUrl += '&role=' + getCookie('UserRoleValue')
                if (getCookie('UserRoleParam') === "0")
                    this.requestUrl += '&role=' + '!' + getCookie('UserRoleValue')
                if (getCookie('UserRoleParam') === "")
                    setCookie('UserRoleValue', '')
            } else
                setCookie('UserRoleValue', '')
        } else {
            setCookie('UserRoleParam', '')
            setCookie('UserRoleValue', '')
        }

        if (getCookie('UserLockedValue')) {
            if (getCookie('UserLockedValue') === '0')
                this.requestUrl += '&locked=' + 0
            else this.requestUrl += '&locked=' + 1
        }
        else setCookie('UserLockedValue', '')
    }

    buildResponse() {
        let request = new XMLHttpRequest();

        request.open('GET', usersManager.requestUrl);
        request.responseType = 'json';
        request.send();
        request.onload = function () {
            let responseObj = request.response;

            for (let i = 0; i < responseObj.length; i++) {
                let tbody = document.createElement('tr');
                tbody.innerHTML += `<td style="text-align: left"><a href="/users/get_user/${responseObj[i].id}">${responseObj[i].fullName}</a></td>`
                tbody.innerHTML += `<td>${responseObj[i].workPhone}</td>`
                tbody.innerHTML += `<td>${responseObj[i].groupName}</td>`
                tbody.innerHTML += `<td>${responseObj[i].username}</td>`
                tbody.innerHTML += `<td>${responseObj[i].role}</td>`
                tbody.innerHTML += `<td>${responseObj[i].locked}</td>`
                tbody.innerHTML += `<td>${responseObj[i].joinDate.replace("T", "&nbsp;&nbsp;&nbsp;")}</td>`
                if (responseObj[i].lastLogin === null) tbody.innerHTML += `<td></td>`
                else tbody.innerHTML += `<td>${responseObj[i].lastLogin.replace("T", "&nbsp;&nbsp;&nbsp;")}</td>`
                document.querySelector('.tbody').appendChild(tbody);
            }
            document.querySelector(".tbody").style.display = 'contents';
            document.querySelector('.spinner').style.display = 'none';
            addSearchEnterListener(usersManager)
            focus()
            usersManager.requestFinished = true
        };

    }

    buildPagesButtons() {
        let url = '/users/rows_count?';
        if (this.searchText !== '')
            url += '&' + this.searchUrl.substring(1) + this.searchText;

        if (getCookie('UserGroupParam') === '1')
            url += '&group=' + getCookie('UserGroupValue')
        if (getCookie('UserGroupParam') === "0")
            url += '&group=' + '!' + getCookie('UserGroupValue')
        if (getCookie('UserRoleParam') === '1')
            url += '&role=' + getCookie('UserRoleValue')
        if (getCookie('UserRoleParam') === "0")
            url += '&role=' + '!' + getCookie('UserRoleValue')
        if (getCookie('UserLockedValue') === '0')
            url += '&locked=' + 0
        if (getCookie('UserLockedValue') === '1')
            url += '&locked=' + 1

        console.log(url)

        let rowsCountRequest = new XMLHttpRequest();
        rowsCountRequest.open('GET', url);

        rowsCountRequest.responseType = 'json';
        rowsCountRequest.send();
        rowsCountRequest.onload = function () {

            let pagesCount = Math.ceil(rowsCountRequest.response / usersManager.resultsPerPage)
            document.getElementById("resultNavigation").innerHTML = '<div id="pagesNavigation"></div>';

            for (let i = 1; i <= pagesCount; i++) {
                if (i === usersManager.pageNumber)
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" style="background-color: rgb(215, 215, 215);" onclick="turnPage(usersManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
                else
                    document.getElementById("pagesNavigation").innerHTML +=
                        `<div class="pagesButtons" onclick="turnPage(usersManager, ${i})"><span style="display: inline-block; margin-top: 3px">${i}</span</div>`
            }
        }
    }
}


