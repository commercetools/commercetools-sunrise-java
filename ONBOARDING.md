
##Structure
All template files are loaded as a [WebJars](http://www.webjars.org/) dependency to Sunrise. This includes [Handlebars](http://handlebarsjs.com/) source templates, all web assets (i.e. CSS, JS, images and fonts) and i18n [YAML](http://www.yaml.org/) files. The expected structure being as follows:

```
META-INF
+-- resources
    +-- webjars
        +-- css
        +-- fonts
        +-- img
        +-- js
        +-- locales
        |   +-- de
        |   +-- en
        |   +-- ..
        +-- templates
```

##Template
Sunrise uses [Handlebars.java](https://jknack.github.io/handlebars.java/) by default as a template engine.

In order to find the corresponding template source file, it searches first inside the classpath `/templates`. If the file is not found there, then it tries inside the Template's Webjars dependency (i.e. `/META-INF/resources/webjars/templates`).

This enables a practical way to override parts of the template without the need of replacing it completely, as we will explain in the next section.

#####Change template source loaders
If you want to change the way templates sources are loaded, you can change the list of template loaders you want to use with `handlebars.templateLoaders`.

Along with the `path`, you can also specify the `type` of path you are providing, which can be either `classpath` or `file`. Keep in mind that the list order determines the order in which the loaders are going to be invoked.

```hocon
handlebars.templateLoaders = [
  {"type":"classpath", "path":"/path/to/files/on/classpath"}, # tries first
  {"type":"file", "path":"relative/path/to/files"}, # tries second, if first failed
  {"type":"file", "path":"/absolute/path/to/files"} # tries third, if all other failed
]
```
 
###How to change the template

This guide shows you how to modify the templates in an easy and convenient way. Note it is assuming the configuration has not been changed.

####Changing the HTML

Run Sunrise and open it in a browser to inspect the part of the code you want to change. In the website's source code you will find comments indicating the template source file containing each component. In the following example we can see that the template source file containing the logo is `common/logo.hbs`.

```html
...
<div class="col-sm-8">
  <!-- start common/logo.hbs -->
  <a href="/en/home" class="brand-logo">
    <img class="img-responsive" src="/assets/img/logo.svg" alt="SUNRISE">
  </a>
  <!-- end common/logo.hbs -->
</div>
...
```

In order to override this component we need to create a Handlebars template source file in `conf/templates` with the desired content, so that the new code is used instead of the original code. To do so, Sunrise offers a convenient [SBT](http://www.scala-sbt.org/) command `copyTemplateFiles`, which given a whitespace-separated list of template files copies them from the original location to `conf/templates`.

Let's execute it for the previous example:

```shell
sbt 'copyTemplateFiles common/logo.hbs'
```

After the execution, `conf/templates/common/logo.hbs` has been created with the content of the original template source file:

```hbs
<!-- start common/logo.hbs -->
<a href="{{@root.meta._links.home.href}}" class="brand-logo">
  <img class="img-responsive" src="{{@root.meta.assetsPath}}img/logo.svg" alt="SUNRISE">
</a>
<!-- end common/logo.hbs -->
```

With that, you are ready to start modifying the template. For example, let's replace the logo image for a simple text:

```hbs
<!-- start common/logo.hbs -->
<a href="{{@root.meta._links.home.href}}" class="brand-logo">
  Sunrise
</a>
<!-- end common/logo.hbs -->
```

If you run Sunrise and reload the page, the image has been effectively replaced by the text:

```html
...
<div class="col-sm-8">
  <!-- start common/logo.hbs -->
  <a href="/en/home" class="brand-logo">
    Sunrise
  </a>
  <!-- end common/logo.hbs -->
</div>
...
```

To learn more about how to write Handlebars templates, please check the [Handlebars.js](http://handlebarsjs.com/) documentation. In particular, the sections about [Expressions](http://handlebarsjs.com/expressions.html), [Built-In Helpers](http://handlebarsjs.com/builtin_helpers.html) and [@data Variables](http://handlebarsjs.com/reference.html#data).

##Miscellanious

Here we list recommended literature and links to learn Sunrise.

######Play Framework
* https://www.playframework.com/documentation/2.4.x/Home
* book http://www.manning.com/leroux/

######Java 8
* book Java SE 8 for the Really Impatient http://www.horstmann.com/java8/index.html

######SBT (Build Tool)
* http://www.scala-sbt.org/0.13/tutorial/index.html
