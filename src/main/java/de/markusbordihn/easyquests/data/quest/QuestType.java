/**
 * Copyright 2023 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easyquests.data.quest;

public enum QuestType {
  // @formatter:off
  CUSTOM,
  MAIN_QUEST,
  SIDE_QUEST,
  EVENT_QUEST,
  DAILY_QUEST,
  WEEKLY_QUEST,
  MONTHLY_QUEST,
  YEARLY_QUEST,
  REPEATABLE_QUEST,
  REPEATABLE_DAILY_QUEST,
  REPEATABLE_WEEKLY_QUEST,
  REPEATABLE_MONTHLY_QUEST,
  REPEATABLE_YEARLY_QUEST;
  // @formatter:on

  public static QuestType get(String questType) {
    if (questType == null || questType.isEmpty()) {
      return QuestType.CUSTOM;
    }
    try {
      return QuestType.valueOf(questType);
    } catch (IllegalArgumentException e) {
      return QuestType.CUSTOM;
    }
  }
}
