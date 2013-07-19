// Copyright (C) 2013  The Weather Channel

package com.weather.cassandra

import com.twitter.cassie.codecs._
import com.twitter.cassie.types.LexicalUUID
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class CompositeUnitTest extends FlatSpec with ShouldMatchers {

  val uuid = new LexicalUUID(System.currentTimeMillis, 0)
  val comps = Array(
    Seq(Component(1, IntCodec), Component("two", Utf8Codec)),
    Seq(Component(1, IntCodec), Component("two", Utf8Codec), Component(5L, LongCodec)),
    Seq(Component(1, IntCodec), Component("two", Utf8Codec), Component(5L, LongCodec), Component("test", Utf8Codec)),
    Seq(Component(1, IntCodec), Component("two", Utf8Codec), Component(5L, LongCodec), Component("test", Utf8Codec), Component(uuid, LexicalUUIDCodec))
  )

  "CompositeCodec" should "properly encode and decode a composite with arity 2" in {
    val c = Composite(comps(0):_*)
    val enc = CompositeCodec.encode(c)
    val dec = CompositeCodec.decode(enc)
    Decoder(IntCodec, Utf8Codec).decode(dec) should equal(DecodedComposite2(comps(0)(0), comps(0)(1)))
  }

  "CompositeCodec" should "properly encode and decode a composite with arity 3" in {
    val c = Composite(comps(1):_*)
    val enc = CompositeCodec.encode(c)
    val dec = CompositeCodec.decode(enc)
    Decoder(IntCodec, Utf8Codec, LongCodec).decode(dec) should equal(DecodedComposite3(comps(1)(0), comps(1)(1), comps(1)(2)))
  }

  "CompositeCodec" should "properly encode and decode a composite with arity 4" in {
    val c = Composite(comps(2):_*)
    val enc = CompositeCodec.encode(c)
    val dec = CompositeCodec.decode(enc)
    Decoder(IntCodec, Utf8Codec, LongCodec, Utf8Codec).decode(dec) should equal(DecodedComposite4(comps(2)(0), comps(2)(1), comps(2)(2), comps(2)(3)))
  }

  "CompositeCodec" should "properly encode and decode a composite with arity 5" in {
    val c = Composite(comps(3):_*)
    val enc = CompositeCodec.encode(c)
    val dec = CompositeCodec.decode(enc)
    Decoder(IntCodec, Utf8Codec, LongCodec, Utf8Codec, LexicalUUIDCodec).decode(dec) should equal(DecodedComposite5(comps(3)(0), comps(3)(1), comps(3)(2), comps(3)(3), comps(3)(4)))
  }

}