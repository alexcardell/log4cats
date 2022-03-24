/*
 * Copyright 2018 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.log4cats.slf4j

import cats.effect.Sync
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.internal._
import org.slf4j.Logger as JLogger
import scala.annotation.nowarn

object Slf4jLogger {

  //for binary compability
  @nowarn("cat=unused")
  private def create[F[_]: Sync]: F[SelfAwareStructuredLogger[F]] = ???

  @nowarn("cat=unused")
  private def getLogger[F[_]](using F: Sync[F]): SelfAwareStructuredLogger[F] = ???

  def getLogger[F[_]: Sync](using n: LoggerName): SelfAwareStructuredLogger[F] =
    getLoggerFromName(n.value.stripSuffix("$"))

  def getLoggerFromName[F[_]: Sync](name: String): SelfAwareStructuredLogger[F] =
    getLoggerFromSlf4j(org.slf4j.LoggerFactory.getLogger(name))

  def getLoggerFromClass[F[_]: Sync](clazz: Class[_]): SelfAwareStructuredLogger[F] =
    getLoggerFromSlf4j[F](org.slf4j.LoggerFactory.getLogger(clazz))

  def getLoggerFromSlf4j[F[_]: Sync](logger: JLogger): SelfAwareStructuredLogger[F] =
    new Slf4jLoggerInternal.Slf4jLogger(logger)

  def create[F[_]: Sync](using n: LoggerName): F[SelfAwareStructuredLogger[F]] =
    Sync[F].delay(getLoggerFromName(n.value.stripSuffix("$")))

  def fromName[F[_]: Sync](name: String): F[SelfAwareStructuredLogger[F]] =
    Sync[F].delay(getLoggerFromName(name))

  def fromClass[F[_]: Sync](clazz: Class[_]): F[SelfAwareStructuredLogger[F]] =
    Sync[F].delay(getLoggerFromClass(clazz))

  def fromSlf4j[F[_]: Sync](logger: JLogger): F[SelfAwareStructuredLogger[F]] =
    Sync[F].delay(getLoggerFromSlf4j[F](logger))

}
