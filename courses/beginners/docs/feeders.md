# Feeders

Feed in data to your simulation e.g. if you are load testing many users logging into your system, you want different users to have different log in credentials. Gatling has inbuilt **feeder types**:

- File based feeders
  - csv
  - tsv
  - ssv
- Others
  - JSON
  - JDBC
- Custom

Along with a feeder we require a **strategy** - in what order should the virtual users receive the (feeder) data. Some strategies are:

- Queue (default)
- Random
- Shuffle
- Circular

[Simulation3](../src/test/scala/com/backwards/gatling/Simulation3.scala) shows the use of a feeder using a csv.

Note the default location for feeder files is directly under **resources** directory. However, this can be changed to say e.g. under **data** (within resources), and for this to work, we have to update the **gatling.conf** file.

We would change:

```json
resources = user-files/resources
```

to

```json
resources = user-files/resources/data
```

[Simulation4](../src/test/scala/com/backwards/gatling/Simulation4.scala) shows an example of a custom feeder.