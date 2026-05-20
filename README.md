# scalajs on nodejs

```bash
## install sjs
npm install

## transpile scalajs to js, using a task defined in package.js
npm run sbtInit   ## downloads sbt into node_modules/sbt (via sjs-nodejs)

npm run sbtBuild  ## needs JRE; runs buildJs (fastOptJS + copy to resources)
```

```bash
$ npm run sjs
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/prayagupd/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/prayagupd/sc212/scalajs-on-nodejs/project
[info] Loading settings from build.sbt ...
[info] Set current project to scalajs-on-nodejs (in build file:/Users/prayagupd/sc212/scalajs-on-nodejs/)
[info] Fast optimizing .../target/scala-3.3.4/scalajs-on-nodejs-fastopt/main.js
[info] Running com.pratyabhi.compliance.Compliance
welcome to Compliance
[success] Total time: 3 s, completed Jun 27, 2018 8:04:28 PM
```

```bash
$ node target/scala-3.3.4/scalajs-on-nodejs-fastopt/main.js
welcome to Compliance
```

# run on nodejs

```bash
npm start
```

go to http://localhost:8080/

The Scala.js app implements the compliance UI (dashboard, sessions, policies, review, reports) with hash routing. Static HTML reference mockups remain at http://localhost:8080/mockups/.
