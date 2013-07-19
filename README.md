This codec allows for operations on composite columns, which is currently not supported in Cassie.  Example usage:

```scala
val cluster = new Cluster("localhost", 9160)
val keyspace = cluster.keyspace("test").connect()
val compositeTest = keyspace.columnFamily("CompositeTest", Utf8Codec, CompositeCodec, Utf8Codec)
                            .consistency(ReadConsistency.One)
                            .consistency(WriteConsistency.One)

val composite = Composite(Component("c1", Utf8Codec), Component(2, LongCodec))
compositeTest.insert("testkey", Column(composite, "testval2"))()

println("row ==")
compositeTest.getRow("testkey")().foreach(c => printCol(c._2))

println("one col ==")
compositeTest.getRowSlice("testkey2",
                          Some(Composite(Component("c1", Utf8Codec, ComponentEquality.EQ))),
                          Some(Composite(Component("c1", Utf8Codec, ComponentEquality.GTE))),
                          Int.MaxValue)()
             .foreach(printCol)

// Decoding a composite
val decoder = Decoder(Utf8Codec, LongCodec)

def printCol(c: Column[Composite, String]) = {
  val colName = decoder.decode(c.name)
  val c1 = colName._1.value
  val c2 = colName._2.value
  println(c1 + ":" + c2  + " = " + c.value)
}
```
