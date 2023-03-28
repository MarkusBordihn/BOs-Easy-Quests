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

import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;

public class QuestManager {

  private static HashMap<ResourceLocation, QuestData> questDataMap = new HashMap<>();

  public static QuestData getQuest(String title) {
    return questDataMap.get(QuestData.getQuestId(title));
  }

  public static QuestData getQuest(ResourceLocation id) {
    return questDataMap.get(id);
  }

  public static boolean hasQuest(String title) {
    return questDataMap.containsKey(QuestData.getQuestId(title));
  }

  public static boolean hasQuest(ResourceLocation id) {
    return questDataMap.containsKey(id);
  }

  public static QuestData createQuest(String title) {
    return createQuest(title, "");
  }

  public static QuestData createQuest(String title, String description) {
    return createQuest(QuestData.getQuestId(title), title, description);
  }

  public static QuestData createQuest(ResourceLocation id, String title) {
    return createQuest(id, title, "");
  }

  public static QuestData createQuest(ResourceLocation id, String title, String description) {
    if (questDataMap.containsKey(id)) {
      return questDataMap.get(id);
    }
    QuestData questData = new QuestData(id, title, description);
    questDataMap.put(id, questData);
    return questData;
  }

}
