[![progress-banner](https://backend.codecrafters.io/progress/shell/ba7057dc-4df7-4f8e-a98f-98d75445b66f)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

This is a starting point for Java solutions to the
["Build Your Own Shell" Challenge](https://app.codecrafters.io/courses/shell/overview).

In this challenge, you'll build your own POSIX compliant shell that's capable of
interpreting shell commands, running external programs and builtin commands like
cd, pwd, echo and more. Along the way, you'll learn about shell command parsing,
REPLs, builtin commands, and more.

**Note**: If you're viewing this repo on GitHub, head over to
[codecrafters.io](https://codecrafters.io) to try the challenge.

# Passing the first stage

The entry point for your `shell` implementation is in `src/main/java/Main.java`.
Study and uncomment the relevant code, and push your changes to pass the first
stage:

```sh
git commit -am "pass 1st stage" # any msg
git push origin master
```

Time to move on to the next stage!

# Stage 2 & beyond

Note: This section is for stages 2 and beyond.

1. Ensure you have `mvn` installed locally
1. Run `./your_program.sh` to run your program, which is implemented in
   `src/main/java/Main.java`.
1. Commit your changes and run `git push origin master` to submit your solution
   to CodeCrafters. Test output will be streamed to your terminal.


---

终端里执行 code 命令无效。

[Visual Studio Code on macOS](https://code.visualstudio.com/docs/setup/mac)
```shell
cat << EOF >> ~/.zprofile
# Add Visual Studio Code (code)
export PATH="\$PATH:/Applications/Visual Studio Code.app/Contents/Resources/app/bin"
EOF
```


安装最新版的 JDK

安装 [jenv](https://github.com/jenv/jenv)

```shell
brew install jenv
```

看下 README 里的教程了解 `add`、`versions`、`local` 和 `global` 等子命令的使用。

[如何安装 OpenJDK？](https://openjdk.org/install/)

[在哪里下载最新版的 OpenJDK？](https://jdk.java.net/23/)

