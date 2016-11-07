# FileRDD

[FileRDD](src/main/java/com/elo7/nightfall/di/providers/file/FileRDD.java) provê data points a partir da leitura de arquivos do S3, file system ou HDFS. É um batch.

## Configurações:

* **file.s3.access.key**: ID da chave de acesso a AWS, opcional.
* **file.s3.secret.key**: segredo da chave de acesso a AWS, opcional.
* **file.source**: Path de onde serão lidos os Data Points. Exemplos:
  * S3: ``s3a://nightfall.elo7.com.br/checkpoints/NightfallJob-*.gz``
  * FileSystem: ``/home/user/checkpoints/NightfallJob-*.gz``
  * HDFS: ``hdfs://data/point/source/*.gz``.
* **file.filter**: Classe para aplicar filtros nos arquivos lidos.

### Filters

Caso não seja especificada uma classe de FileFilter nenhum filtro será aplicado.

#### FileNameDateFilter

Filtra sources que possuam uma data ("YYY-MM-dd") em seu path.

Este filtro efetua o replace de ``${DATE}`` encontrados no source por datas. Exemplo: um source com ``/tmp/dt=${DATE}/*.gz`` será subistiuido por um intervalo de datas, resultando em: ``/tmp/dt=2016-05-16/*.gz``.

**Configurações**:

* **file.filter.name.window.size.days**: Janela de dias aceitos. Default ``1``, valor minimo ``1``.
* **file.filter.name.window.slide.days**: Número de dias descartados do final da janela. Default ``1``, valor minimo ``0``.
* **file.filter.name.window.date.end**: Data final da janela. Default data atual.

Exemplos:

 * Janela com os ultimos três dias, ``file.source=/tmp/dt=${DATE}/``:

```
file.filter.name.window.size.days=3
file.filter.name.window.slide.days=0
file.filter.name.window.date.end=2016-05-03
```

Source ficará: ``/tmp/dt=2016-05-13,/tmp/dt=2016-05-14,/tmp/dt=2016-05-15``.

* Janela de cinco dias atrás até dois dias atrás, ``file.source=/tmp/dt=${DATE}/``:

```
file.filter.name.window.size.days=3
file.filter.name.window.slide.days=2
file.filter.name.window.date.end=2016-05-15
```

Source ficará: ``/tmp/dt=2016-05-11,/tmp/dt=2016-05-12,/tmp/dt=2016-05-13``.
