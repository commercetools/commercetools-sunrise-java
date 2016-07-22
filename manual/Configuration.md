# Configuration

## Configure product search

Without the need of touching a single line of Java code, you can easily modify how products can be filtered, sorted and displayed. You only have to adapt [`application.conf`](https://github.com/commercetools/commercetools-sunrise-java/blob/master/conf/application.conf) as needed through the following configuration parameters:

- `pop.pagination`: to configure how the pagination works
- `pop.productsPerPage`: to define the amount of products listed per page
- `pop.sortProducts`: to specify the different sort options
- `pop.facets`: to configure the different product filtering options

You can find further information about each parameter in the file itself.

## Configure Handlebars

### Change template source loaders

If you want to change the way templates sources are loaded, you can change the list of template loaders you want to use with `handlebars.templateLoaders`.

Along with the `path`, you also need to specify the `type` of path you are providing, which can be either `classpath` or `file`. Keep in mind that the list order determines the order in which the loaders are going to be invoked.

```hocon
handlebars.templateLoaders = [
  {"type":"classpath", "path":"/path/to/files/on/classpath"}, # tries first
  {"type":"file", "path":"relative/path/to/files"}, # tries second, if first failed
  {"type":"file", "path":"/absolute/path/to/files"} # tries third, if all other failed
]
```

## Configure localization

### Change i18n source loaders

If you want to change the way i18n messages are resolved, you can change the list of resolver loaders you want to use with `application.i18n.resolverLoaders`.

Along with the `path`, you also need to specify the `type` of file you are providing, being the only currently supported option `yaml`, for YAML files. Keep in mind that the list order determines the order in which the resolvers are going to be invoked for each message.

```hocon
application.i18n.resolverLoaders = [
  {"type":"yaml", "path":"/path/to/files/"}, # tries first
  {"type":"yaml", "path":"/path/to/other/files"}, # tries second, if first failed
  {"type":"yaml", "path":"/path/to/even/other/files"} # tries third, if all other failed
]
```
