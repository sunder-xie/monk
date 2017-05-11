/**
 * Created by huangzhangting on 16/1/29.
 */

/* 引入gulp插件 */
var gulp = require('gulp');
var uglify = require('gulp-uglify');
var minifyCss = require('gulp-minify-css');
var rev = require('gulp-rev');
var revReplace = require('gulp-rev-replace');
var clean = require('gulp-clean');
var rename = require('gulp-rename');
//var concat = require('gulp-concat');
var notify = require("gulp-notify");
var useref = require('gulp-useref');
var gulpif = require('gulp-if');
var replace = require('gulp-replace');


/* 定义一些路径常量 */
var webappPath = 'web/src/main/webapp/';
var devFile = 'monk-dev/';
var devPath = webappPath + devFile;

var revPath = devPath + 'manifest/';

var releaseFile = 'monk-release/';
var releasePath = webappPath + releaseFile;

var pageFile = 'monk/';

var specialFile = '/test_ftl/';


/* 编写gulp任务 */
gulp.task('help', function(){
    var tab = '    ';
    console.log(tab+'gulp help   :  帮助');
    console.log(tab+'gulp build  :  构建前端模块');
});

gulp.task('default', ['help']);

/* 清除上次的结果 */
gulp.task('clean', function(){
    return gulp.src([releasePath, revPath], {read: false})
        .pipe(clean());
});

/*对外提供的不需要加版本号*/
gulp.task('special', ['clean'], function(){
    var filePath = process.cwd()+'/'+webappPath;


    return gulp.src(devPath + pageFile+specialFile + '*.ftl')
        .pipe(useref({
            searchPath: webappPath
        }))
        .pipe(gulpif('*.js', uglify()))
        .pipe(gulpif('*.css', minifyCss()))
        //因为这边统一输出，合并后的js、css会和页面在同一路径下，因此需要特殊处理js、css，该项目中处理如下
        // <!-- build:css ../resources/concat/concat-css.css -->
        .pipe(gulp.dest(releasePath + pageFile))
        .pipe(rev.manifest())
        .pipe(replace(filePath, ''))
        .pipe(gulp.dest(revPath + 'concat'));
});

/* 处理资源，压缩合并js、css--monk单文件目录下 */
gulp.task('concat', ['special'], function(){
    var filePath = process.cwd()+'/'+webappPath;


    return gulp.src(devPath + pageFile + '*.ftl')
        .pipe(useref({
            searchPath: webappPath
        }))
        .pipe(gulpif('*.js', uglify()))
        .pipe(gulpif('*.js', rev()))
        .pipe(gulpif('*.css', minifyCss()))
        .pipe(gulpif('*.css', rev()))
        //因为这边统一输出，合并后的js、css会和页面在同一路径下，因此需要特殊处理js、css，该项目中处理如下
        // <!-- build:css ../resources/concat/concat-css.css -->
        .pipe(gulp.dest(releasePath + pageFile))
        .pipe(rev.manifest())
        .pipe(replace(filePath, ''))
        .pipe(gulp.dest(revPath + 'concat'));
});

/* 替换开发路径、资源版本 */
gulp.task('build', ['concat'], function(){
    var manifest = gulp.src(revPath + 'concat/*.json');

    return gulp.src(releasePath + pageFile + '**/*.ftl')
        .pipe(replace('../', '/'+releaseFile))
        .pipe(replace(devFile, releaseFile))
        .pipe(revReplace({
            manifest: manifest,
            replaceInExtensions: ['.jsp', '.ftl', '.html', '.js', '.css']
        }))
        .pipe(gulp.dest(releasePath + pageFile))
        .pipe(notify('build success'));
});



gulp.task('test', function(){
    var tempPath = devPath+'resources/common/timeline/';
    return gulp.src(tempPath+'*.css')
        .pipe(minifyCss())
        .pipe(rename(function(path){
            path.basename += ".min";
        }))
        .pipe(gulp.dest(tempPath));
});