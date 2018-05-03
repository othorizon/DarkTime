const process = require('child_process');
// const fs = require('fs')

module.exports = {
    hooks: {
        init: function () {
            const root = this.resolve('')
            console.log("start pull submodule");
            //直接调用命令
            process.execSync("git submodule init;git submodule update",
                function (error, stdout, stderr) {
                    console.log(stdout);
                    if (error !== null) {
                        // console.log('stdout: ' + stdout);
                        console.log('stderr: ' + stderr);
                        console.log('exec error: ' + error);
                    }
                });
            console.log("pull submodule finish");

        }
    }
}