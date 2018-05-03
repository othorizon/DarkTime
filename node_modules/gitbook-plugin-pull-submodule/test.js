// var process = require('child_process');



// //直接调用命令
// process.execSync("./buildsummary.sh \"./\"",
//     function (error, stdout, stderr) {
//         console.log('stdout: ' + stdout);
//         console.log('stderr: ' + stderr);
//         console.log('exec error: ' + error);
//     });


//做饭
function cook(){
    console.log('开始做饭。');
    var p = new Promise(function(resolve, reject){        //做一些异步操作
        setTimeout(function(){
            console.log('做饭完毕！');
            resolve('鸡蛋炒饭');
        }, 1000);
    });
    return p;
}
 
//吃饭
function eat(data){
    console.log('开始吃饭：' + data);
    var p = new Promise(function(resolve, reject){        //做一些异步操作
        setTimeout(function(){
            console.log('吃饭完毕!');
            resolve('一块碗和一双筷子');
        }, 2000);
    });
    return p;
}
 
function wash(data){
    console.log('开始洗碗：' + data);
    var p = new Promise(function(resolve, reject){        //做一些异步操作
        setTimeout(function(){
            console.log('洗碗完毕!');
            resolve('干净的碗筷');
        }, 2000);
    });
    return p;
}

cook()
.then(function(data){
    return eat(data);
})
.then(function(data){
    return wash(data);
})
.then(function(data){
    console.log(data);
});
console.log("aaa")
console.log("bbbb")