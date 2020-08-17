var expanded = false;

function refreshTable(tableId, selectId, select2Id, inputId) {
    var loaderId = (tableId === 'table1') ? '#loader1' : '#loader2';
    $(loaderId).show();
    var buttonId = (tableId === 'table1') ? '#refreshButton' : '#calculateButton';
    $(buttonId).prop("disabled", true);
    var table = document.getElementById(tableId);
    var from = document.getElementById(selectId).value;
    var url = "exchange?from=" + from;
    if (select2Id != null && inputId != null) {
        if (expanded) {
            showCheckboxes();
        }
        var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');
        var to = Array.prototype.map.call(checkboxes, el => el.id);
        console.log(to);
        var amount = document.getElementById(inputId).value;
        url += "&to=" + to + "&amount=" + amount;
    }
    console.log(from);
    table.innerHTML = null;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var row = table.insertRow(0);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            cell1.innerHTML = "Coin";
            cell2.innerHTML = "Rate";
            console.log(this.responseText);
            var objects = JSON.parse(this.responseText);
            objects.forEach( element => {
                row = table.insertRow(-1);
                cell1 = row.insertCell(0);
                cell2 = row.insertCell(1);
                cell1.innerHTML = element.to;
                cell2.innerHTML = element.exchangeAmount;
            });
            $(loaderId).hide();
            $(buttonId).prop("disabled", false);
        }
    };
    xhttp.open("GET", url, true);
    xhttp.send();
};
function setSelectLists(){
    var select1 = document.getElementById("select1");
    var select2 = document.getElementById("select2");
    var select3 = document.getElementById("checkboxes");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var objects = JSON.parse(this.responseText);
            objects.forEach( element => {
                var option1 = document.createElement("option");
                var option2 = document.createElement("option");
                var option3 = '<label for="' + element + '"><input type="checkbox" id="' + element + '" />' + element + '</label>';
                option1.text = element;
                option2.text = element;
                option1.value = element;
                option2.value = element;
                select1.add(option1);
                select2.add(option2);
                select3.innerHTML += option3;
            });
            select1.value = "EUR";
            select2.value = "EUR";
            select3.value = "EUR";
            refreshTable("table1", "select1");
        }
    };
    xhttp.open("GET", "currencies", true);
    xhttp.send();
};

function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}
