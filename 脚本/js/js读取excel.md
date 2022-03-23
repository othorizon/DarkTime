# sheetjs-js读取excel

参考文档：
[GitHub - rockboom/SheetJS-docs-zh-CN: SheetJS中文文档，版本v0.14.0，持续更新中](https://github.com/rockboom/SheetJS-docs-zh-CN)
[GitHub - SheetJS/sheetjs: SheetJS Community Edition -- Spreadsheet Data Toolkit](https://github.com/SheetJS/sheetjs)
[使用 js-xlsx 处理 Excel 文件 - 星陨的菲 - 博客园](https://www.cnblogs.com/unreal-feather/p/12794129.html?ivk_sa=1024320u)

示例代码

```html
<!DOCTYPE html>
<html lang="zh-cn">

<head>
    <meta charset="UTF-8">
    <title>权限点文件生成</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
        }

        label {
            padding-left: 20px;
        }

        th,
        td {
            border: solid 1px #6D6D6D;
            padding: 5px 10px;
        }

        .mt-sm {
            margin-top: 8px;
        }

        body {
            background: #f4f4f4;
            padding: 0;
            margin: 0;
        }

        .container {
            width: 1024px;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            min-height: 100vh;
        }
    </style>
</head>

<body>
    <div class="container">
        <h1>步骤一：读取excel</h1>
        <div class="mt-sm">
            <input type="file" id="file" style="display:none;"
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
            <a href="javascript:selectFile()">加载本地excel文件</a>
            <div id="filePath"></div>
        </div>

        <h1>步骤二：选择套餐和项目</h1>
        <p>套餐</p>
        <div id="taocan"></div>
        <p>项目</p>
        <div id="project"></div>

        <h1>步骤三：导出权限点制品</h1>
        <div class="mt-sm" style="padding-bottom:40px;">
            <input id="exportFile" type="button" onclick="exportExcel()" value="导出权限点制品" />
        </div>
    </div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/xlsx/0.18.4/xlsx.core.min.js"></script>
    <script type="text/javascript">

        function selectFile() {
            document.getElementById('file').click();
        }

        // 读取本地excel文件
        function readWorkbookFromLocalFile(file, callback) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var data = e.target.result;
                var workbook = XLSX.read(data, { type: 'binary' });
                if (callback) callback(workbook);
            };
            reader.readAsBinaryString(file);
        }


        // 读取 excel文件
        function outputWorkbook(workbook) {
            var sheetNames = workbook.SheetNames; // 工作表名称集合
            sheetNames.forEach(name => {
                var worksheet = workbook.Sheets[name]; // 只能通过工作表名称来获取指定工作表
                for (var key in worksheet) {
                    // v是读取单元格的原始值
                    console.log(key, key[0] === '!' ? worksheet[key] : worksheet[key].v);
                }
            });
        }
        let _worksheets;
        let taocan = [];
        let projects = [];
        let selectedTaocan = null;
        let selectedProject = null;
        function readWorkbook(workbook) {

            // var sheetNames = workbook.SheetNames; // 工作表名称集合
            var worksheet = workbook.Sheets["平台权限"]; // 这里我们只读取第一张sheet
            console.log(worksheet)
            _worksheets = workbook.Sheets;
            //删除筛选
            delete worksheet["!autofilter"];
            //获取套餐
            readTaocan(worksheet);
            renderTaocan();
            //获取项目
            readProjects(worksheet);
            renderProjects();
            //清理公式
            formual2txt(worksheet);


            // var csv = XLSX.utils.sheet_to_csv(worksheet);
            // document.getElementById('result').innerHTML = csv2table(csv);
        }
        function renderTaocan() {
            selectedTaocan = null;
            let content = "";
            for (let t in taocan) {
                content += '<label>' + taocan[t] + '</label><input name="taocanRadio" type="radio" id="' + taocan[t] + '">'
            }
            $('#taocan').html(content);
            $('input:radio[name="taocanRadio"]').change(function (e) {
                console.log(e.target.id);
                selectedTaocan = e.target.id;
            })
        }
        function renderProjects() {
            selectedProject = null;
            let content = "";
            for (let t in projects) {
                content += '<label>' + projects[t] + '</label><input name="projectRadio" type="radio" id="' + projects[t] + '">'
            }
            $('#project').html(content);
            $('input:radio[name="projectRadio"]').change(function (e) {
                console.log(e.target.id);
                selectedProject = e.target.id;
            })
            $('input:radio[id="标品"]').attr("checked", true);
            selectedProject = "标品";
        }
        function readTaocan(ws) {
            taocan = [];
            const range = XLSX.utils.decode_range(ws['!ref']);
            for (let i = 3; i <= range.e.r + 1; i++) {//第三行开始
                if (ws["F" + i] && ws["F" + i]["w"]) {
                    if (taocan.indexOf(ws["F" + i]["w"]) < 0) {
                        taocan.push(ws["F" + i]["w"]);
                    }
                }
            }
        }
        function readProjects(ws) {
            projects = [];
            const range = XLSX.utils.decode_range(ws['!ref']);
            for (let i = 3; i <= range.e.r + 1; i++) {//第三行开始
                if (ws["G" + i] && ws["G" + i]["w"]) {
                    ws["G" + i]["w"].split(";").forEach(e => {
                        if (projects.indexOf(e) < 0) {
                            projects.push(e);
                        }
                    });
                }
            }
        }

        function formual2txt(ws) {
            const range = XLSX.utils.decode_range(ws['!ref']);
            for (let col = range.s.c; col <= range.e.c; col++) {
                for (let row = range.s.r; row <= range.e.r; row++) {
                    let address = encodeCell(row, col);
                    //删除公式
                    if (ws[address] && ws[address]['f']) {
                        delete ws[address]['f'];
                    }
                }
            }
        }
        function delCols() {
            var worksheet = _worksheets["平台权限"];
            //删除0-7列
            for (i = 0; i < 7; i++) {
                deleteCol(worksheet, 0);
            }
            //迭代-环境 6个
            for (i = 0; i < 6; i++) {
                deleteCol(worksheet, 5);
            }
            //角色 12个
            for (i = 0; i < 12; i++) {
                deleteCol(worksheet, 18);
            }
        }
        function encodeCell(r, c) {
            return XLSX.utils.encode_cell({ r, c });
        }
        function deleteRow(ws, index) {
            const range = XLSX.utils.decode_range(ws['!ref']);

            for (let row = index; row < range.e.r; row++) {
                for (let col = range.s.c; col <= range.e.c; col++) {
                    ws[encodeCell(row, col)] = ws[encodeCell(row + 1, col)];
                }
            }

            range.e.r--;

            ws['!ref'] = XLSX.utils.encode_range(range.s, range.e);
        }
        function deleteCol(ws, index) {
            const range = XLSX.utils.decode_range(ws['!ref']);

            for (let col = index; col < range.e.c; col++) {
                for (let row = range.s.r; row <= range.e.r; row++) {
                    ws[encodeCell(row, col)] = ws[encodeCell(row, col + 1)];
                }
            }

            range.e.c--;

            ws['!ref'] = XLSX.utils.encode_range(range.s, range.e);
        }
        // 将csv转换成表格
        function csv2table(csv) {
            var html = '<table>';
            var rows = csv.split('\n');
            rows.pop(); // 最后一行没用的
            rows.forEach(function (row, idx) {
                var columns = row.split(',');
                columns.unshift(idx + 1); // 添加行索引
                if (idx == 0) { // 添加列索引
                    html += '<tr>';
                    for (var i = 0; i < columns.length; i++) {
                        html += '<th>' + (i == 0 ? '' : String.fromCharCode(65 + i - 1)) + '</th>';
                    }
                    html += '</tr>';
                }
                html += '<tr>';
                columns.forEach(function (column) {
                    html += '<td>' + column + '</td>';
                });
                html += '</tr>';
            });
            html += '</table>';
            return html;
        }

        function table2csv(table) {
            var csv = [];
            $(table).find('tr').each(function () {
                var temp = [];
                $(this).find('td').each(function () {
                    temp.push($(this).html());
                })
                temp.shift(); // 移除第一个
                csv.push(temp.join(','));
            });
            csv.shift();
            return csv.join('\n');
        }

        // csv转sheet对象
        function csv2sheet(csv) {
            var sheet = {}; // 将要生成的sheet
            csv = csv.split('\n');
            csv.forEach(function (row, i) {
                row = row.split(',');
                if (i == 0) sheet['!ref'] = 'A1:' + String.fromCharCode(65 + row.length - 1) + (csv.length - 1);
                row.forEach(function (col, j) {
                    sheet[String.fromCharCode(65 + j) + (i + 1)] = { v: col };
                });
            });
            return sheet;
        }

        // 导出配置
        function sheet2blob() {

            var workbook = {
                SheetNames: ['平台权限', '平台角色'],
                Sheets: { '平台权限': _worksheets['平台权限'], '平台角色': _worksheets['平台角色'] }
            };

            // 生成excel的配置项
            var wopts = {
                bookType: 'xlsx', // 要生成的文件类型
                bookSST: false, // 是否生成Shared String Table，官方解释是，如果开启生成速度会下降，但在低版本IOS设备上有更好的兼容性
                type: 'binary'
            };
            var wbout = XLSX.write(workbook, wopts);
            var blob = new Blob([s2ab(wbout)], { type: "application/octet-stream" });
            // 字符串转ArrayBuffer
            function s2ab(s) {
                var buf = new ArrayBuffer(s.length);
                var view = new Uint8Array(buf);
                for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            }
            return blob;
        }

        /**
         * 通用的打开下载对话框方法，没有测试过具体兼容性
         * @param url 下载地址，也可以是一个blob对象，必选
         * @param saveName 保存文件名，可选
         */
        function openDownloadDialog(url, saveName) {
            if (typeof url == 'object' && url instanceof Blob) {
                url = URL.createObjectURL(url); // 创建blob地址
            }
            var aLink = document.createElement('a');
            aLink.href = url;
            aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效
            var event;
            if (window.MouseEvent) event = new MouseEvent('click');
            else {
                event = document.createEvent('MouseEvents');
                event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            }
            aLink.dispatchEvent(event);
        }
        function delNotIncludeRows() {
            var ws = _worksheets["平台权限"];
            if (!selectedTaocan && !confirm("没有选择套餐，将会导出所有权限点，是否继续？")) {
                return "cancel";
            }
            //第三行开始
            for (; ;) {
                let r = delRowNext(ws, 3);
                if (r > 0) {
                    delRowNext(ws, r);
                } else {
                    break;
                }
            }
        }
        function delRowNext(ws, startRow) {
            const range = XLSX.utils.decode_range(ws['!ref']);
            for (let i = startRow; i <= range.e.r + 1; i++) {//第二行开始
                //套餐清理
                let address = "F" + i;
                if (selectedTaocan === "低") {
                    if (ws[address] && (ws[address]["w"] === "中" || ws[address]["w"] === "高")) {
                        deleteRow(ws, i - 1);
                        return i;
                    }
                } else if (selectedTaocan === "中") {
                    if (ws[address] && (ws[address]["w"] === "高")) {
                        deleteRow(ws, i - 1);
                        return i;
                    }
                }

                //项目清理
                address = "G" + i;
                if (ws[address] && ws[address]["w"]) {
                    let val = ws[address]["w"];
                    if (val.indexOf("标品") < 0 && val.indexOf(selectedProject) < 0) {
                        console.log("del", val, i - 1);
                        deleteRow(ws, i - 1);
                        return i;
                    }
                }
            }
            return -1;
        }

        let _file;
        $(function () {
            document.getElementById("exportFile").disabled = true;
            document.getElementById('file').addEventListener('change', function (e) {
                var files = e.target.files;
                if (files.length == 0) return;
                var f = files[0];
                if (!/\.xlsx$/g.test(f.name)) {
                    alert('仅支持读取xlsx格式!');
                    return;
                }
                _file = f;
                $('#filePath').html(_file.name);
                readWorkbookFromLocalFile(f, function (workbook) {
                    readWorkbook(workbook);
                    document.getElementById("exportFile").disabled = false;
                });
            });
        });


        function exportExcel() {
            document.getElementById("exportFile").disabled = true;
            document.getElementById("exportFile").value = "导出中...";
            // $('#exportFile').val("导出中...");
            //清理非套餐和项目数据
            let result = delNotIncludeRows();
            if (result === "cancel") {
                document.getElementById("exportFile").value = "导出权限点制品";
                document.getElementById("exportFile").disabled = false;
                return;
            }

            //删除无用的列
            delCols();
            var blob = sheet2blob();
            openDownloadDialog(blob, '企业云-' + selectedProject + '-套餐' + selectedTaocan + '-权限点制品.xlsx');

            //初始化
            readWorkbookFromLocalFile(_file, function (workbook) {
                readWorkbook(workbook);
                document.getElementById("exportFile").value = "导出权限点制品";
                document.getElementById("exportFile").disabled = false;
            });
        }

    </script>
</body>

</html>
```
