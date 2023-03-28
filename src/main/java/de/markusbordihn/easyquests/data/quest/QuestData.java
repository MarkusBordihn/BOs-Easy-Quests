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

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import de.markusbordihn.easyquests.Constants;
import de.markusbordihn.easyquests.data.criteria.CriteriaData;
import de.markusbordihn.easyquests.data.reward.RewardData;

public class QuestData {

  // Main quest data
  public final ResourceLocation id;
  public final String title;

  // Meta data
  private List<CriteriaData> criterias = null;
  private List<RewardData> rewards = null;
  private QuestCategory category = QuestCategory.NONE;
  private QuestDifficulty difficulty = QuestDifficulty.NORMAL;
  private QuestType type = QuestType.CUSTOM;
  private String description = "";

  public QuestData(String title) {
    this(title, "");
  }

  public QuestData(String title, String description) {
    this(new ResourceLocation(Constants.QUEST_NAMESPACE, "quests/" + normalizeResourceName(title)),
        title, description);
  }

  public QuestData(ResourceLocation id, String title) {
    this(id, title, "");
  }

  public QuestData(ResourceLocation id, String title, String description) {
    this.id = id;
    this.title = title;
    this.description = description;
  }

  public ResourceLocation getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public QuestCategory getCategory() {
    return this.category;
  }

  public void setCategory(QuestCategory category) {
    this.category = category;
  }

  public QuestDifficulty getDifficulty() {
    return this.difficulty;
  }

  public void setDifficulty(QuestDifficulty difficulty) {
    this.difficulty = difficulty;
  }

  public QuestType getType() {
    return this.type;
  }

  public void setType(QuestType type) {
    this.type = type;
  }

  public List<CriteriaData> getCriterias() {
    return this.criterias;
  }

  public void setCriterias(List<CriteriaData> criterias) {
    this.criterias = criterias;
  }

  public List<RewardData> getRewards() {
    return this.rewards;
  }

  public void setRewards(List<RewardData> rewards) {
    this.rewards = rewards;
  }

  public static ResourceLocation getQuestId(String title) {
    return new ResourceLocation(Constants.QUEST_NAMESPACE,
        "quests/" + normalizeResourceName(title));
  }

  public static String normalizeResourceName(String text) {
    return text.toLowerCase().replace(" ", "_").replaceAll("\\W", "");
  }

  @Override
  public String toString() {
    return "QuestData [id=" + this.id + ", title=" + this.title + ", description="
        + this.description + ", category=" + this.category + ", difficulty=" + this.difficulty
        + ", type=" + this.type + ", criterias=" + this.criterias + ", rewards=" + this.rewards
        + "]";
  }

}
