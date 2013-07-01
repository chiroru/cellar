'use strict';

var path = require('path');
var lrSnippet = require('grunt-contrib-livereload/lib/utils').livereloadSnippet;
var folderMount = function(connect, point) {
    return connect.static(path.resolve(point));
};

module.exports = function(grunt){
  var pkg = grunt.file.readJSON('package.json');

  grunt.initConfig({
    bower: {
      install: {
        options: {
          targetDir: 'js/lib',
          install: true,
          verbose: true,
          cleanTargetDir: true,
          cleanBowerDir: true
        }
      }
    },

    connect: {
      livereload: {
        options: {
          port: 9001,
          middleware: function(connect, options) {
            return [lrSnippet, folderMount(connect, '.')];
          }
        }
      }
    },

    regarde: {
      html: {
        files: ['**/*.html'],
        tasks: ['livereload']
      },
      css: {
        files: ['src/**/*.css', 'test/**/*.css'],
        tasks: ['livereload']
      },
      js: {
        files: ['js/**/*.js', 'js-test/**/*.js'],
        tasks: ['livereload']
      }
    },
    qunit: {
      all: ['js-test/**/*.html']
    },
    jsdoc: {
      dist: {
        src: ['src/**/*.js'],
        options: {
          destination: 'doc'
        }
      }
    }
  });

  var contrib;
  for (contrib in pkg.devDependencies) {
    if (contrib.substring(0, 6) === 'grunt-') {
      console.log(contrib);
      grunt.loadNpmTasks(contrib);
    }
  }

  grunt.registerTask('init', ['bower:install']);
  grunt.registerTask('deploy', ['livereload-start', 'connect', 'regarde']);
  grunt.registerTask('doc', ['jsdoc']);
  grunt.registerTask('default', ['jsdoc']);
};
