// Copyright (c) 2021 by Vadim Bondarev
// This software is licensed under the Apache License, Version 2.0.
// You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

package yoctodb

import yoctodb.schema.games.v1.GamesSchema

sealed trait Ops[T]

object EmptyTermOps extends Ops[Nothing]

//com.yandex.yoctodb.mutable.DocumentBuilder.IndexOption.FILTERABLE -- the field can be used to filter documents
trait Filterable[T] extends Ops[T]:

  def eq$(v: T): com.yandex.yoctodb.query.TermCondition

  def notEq$(v: T): com.yandex.yoctodb.query.Condition =
    com.yandex.yoctodb.query.QueryBuilder.not(eq$(v))

  def in$(vs: scala.collection.immutable.Set[T]): com.yandex.yoctodb.query.TermCondition

trait FilterableNum[T](using n: Numeric[T]) extends Filterable[T]:

  def gt$(v: T): com.yandex.yoctodb.query.TermCondition

  def gte$(v: T): com.yandex.yoctodb.query.TermCondition

  def lt$(v: T): com.yandex.yoctodb.query.TermCondition

  def lte$(v: T): com.yandex.yoctodb.query.TermCondition

//com.yandex.yoctodb.mutable.DocumentBuilder.IndexOption.SORTABLE -- the field can be used to sort documents
trait Sortable[T] extends Ops[T]:

  def descOrd: com.yandex.yoctodb.query.Order

  def ascOrd: com.yandex.yoctodb.query.Order

//FILTERABLE and SORTABLE
trait BothNums[T](using n: Numeric[T]) extends FilterableNum[T] with Sortable[T]

//FILTERABLE and SORTABLE
trait BothChars[T](using T <:< java.lang.CharSequence) extends Filterable[T] with Sortable[T]
