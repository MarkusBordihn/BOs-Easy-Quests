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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.registries.ForgeRegistries;

import de.markusbordihn.easyquests.Constants;
import de.markusbordihn.easyquests.data.criteria.CriteriaData;
import de.markusbordihn.easyquests.data.reward.RewardData;

public class QuestData {

  // Data Tags
  public static final String DATA_QUEST_BACKGROUND_TAG = "Background";
  public static final String DATA_QUEST_CATEGORY_TAG = "Category";
  public static final String DATA_QUEST_CRITERIAS_TAG = "Criterias";
  public static final String DATA_QUEST_DESCRIPTION_COLOR_TAG = "DescriptionColor";
  public static final String DATA_QUEST_DESCRIPTION_TAG = "Description";
  public static final String DATA_QUEST_DIFFICULTY_TAG = "Difficulty";
  public static final String DATA_QUEST_ICON_TAG = "Icon";
  public static final String DATA_QUEST_ID_TAG = "Id";
  public static final String DATA_QUEST_REWARDS_TAG = "Rewards";
  public static final String DATA_QUEST_TITLE_COLOR_TAG = "TitleColor";
  public static final String DATA_QUEST_TITLE_TAG = "Title";
  public static final String DATA_QUEST_TYPE_TAG = "Type";

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

  // Styling
  private ItemStack icon;
  private ResourceLocation background;
  private int descriptionColor = 0xFFCCCCCC;
  private int titleColor = 0xFFFFFFFF;

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

  public ItemStack getIcon() {
    return this.icon;
  }

  public void setIcon(ItemStack icon) {
    this.icon = icon;
  }

  public void setIcon(ResourceLocation icon) {
    this.icon = ForgeRegistries.ITEMS.getValue(icon).getDefaultInstance();
  }

  public ResourceLocation getBackground() {
    return this.background;
  }

  public void setBackground(ResourceLocation background) {
    this.background = background;
  }

  public int getDescriptionColor() {
    return this.descriptionColor;
  }

  public void setDescriptionColor(int descriptionColor) {
    this.descriptionColor = descriptionColor;
  }

  public int getTitleColor() {
    return this.titleColor;
  }

  public void setTitleColor(int titleColor) {
    this.titleColor = titleColor;
  }

  public static ResourceLocation getQuestId(String title) {
    return new ResourceLocation(Constants.QUEST_NAMESPACE,
        "quests/" + normalizeResourceName(title));
  }

  public static String normalizeResourceName(String text) {
    return text.toLowerCase().replace(" ", "_").replaceAll("\\W", "");
  }

  public static QuestData load(CompoundTag compoundTag) {
    QuestData questData =
        new QuestData(
          new ResourceLocation(compoundTag.getString(DATA_QUEST_ID_TAG)),
          compoundTag.getString(DATA_QUEST_TITLE_TAG),
          compoundTag.getString(DATA_QUEST_DESCRIPTION_TAG)
        );

    // Quest meta data.
    if (compoundTag.contains(DATA_QUEST_CATEGORY_TAG)) {
      questData.setCategory(QuestCategory.valueOf(compoundTag.getString(DATA_QUEST_CATEGORY_TAG)));
    }
    if (compoundTag.contains(DATA_QUEST_DIFFICULTY_TAG)) {
      questData
          .setDifficulty(QuestDifficulty.valueOf(compoundTag.getString(DATA_QUEST_DIFFICULTY_TAG)));
    }
    if (compoundTag.contains(DATA_QUEST_TYPE_TAG)) {
      questData.setType(QuestType.valueOf(compoundTag.getString(DATA_QUEST_TYPE_TAG)));
    }

    // Quest criterias and rewards.
    if (compoundTag.contains(DATA_QUEST_CRITERIAS_TAG)) {
      ListTag criteriasTag = compoundTag.getList(DATA_QUEST_CRITERIAS_TAG, 10);
      List<CriteriaData> criterias = new ArrayList<>();
      for (int i = 0; i < criteriasTag.size(); i++) {
        CompoundTag criteriaTag = criteriasTag.getCompound(i);
        CriteriaData criteriaData = CriteriaData.load(criteriaTag);
        criterias.add(criteriaData);
      }
      questData.setCriterias(criterias);
    }
    if (compoundTag.contains(DATA_QUEST_REWARDS_TAG)) {
      ListTag rewardsTag = compoundTag.getList(DATA_QUEST_REWARDS_TAG, 10);
      List<RewardData> rewards = new ArrayList<>();
      for (int i = 0; i < rewardsTag.size(); i++) {
        CompoundTag rewardTag = rewardsTag.getCompound(i);
        RewardData rewardData = RewardData.load(rewardTag);
        rewards.add(rewardData);
      }
      questData.setRewards(rewards);
    }

    // Quest Visuals
    if (compoundTag.contains(DATA_QUEST_ICON_TAG)) {
      questData.setIcon(ItemStack.of(compoundTag.getCompound(DATA_QUEST_ICON_TAG)));
    }
    if (compoundTag.contains(DATA_QUEST_BACKGROUND_TAG)) {
      questData.setBackground(new ResourceLocation(compoundTag.getString(DATA_QUEST_BACKGROUND_TAG)));
    }
    if (compoundTag.contains(DATA_QUEST_DESCRIPTION_COLOR_TAG)) {
      questData.setDescriptionColor(compoundTag.getInt(DATA_QUEST_DESCRIPTION_COLOR_TAG));
    }
    if (compoundTag.contains(DATA_QUEST_TITLE_COLOR_TAG)) {
      questData.setTitleColor(compoundTag.getInt(DATA_QUEST_TITLE_COLOR_TAG));
    }
    return questData;
  }

  public CompoundTag save(CompoundTag compoundTag) {
    compoundTag.putString(DATA_QUEST_ID_TAG, this.id.toString());
    compoundTag.putString(DATA_QUEST_TITLE_TAG, this.title);
    compoundTag.putString(DATA_QUEST_DESCRIPTION_TAG, this.description);

    // Quest meta data.
    if (this.category != null) {
      compoundTag.putString(DATA_QUEST_CATEGORY_TAG, this.category.name());
    }
    if (this.difficulty != null) {
      compoundTag.putString(DATA_QUEST_DIFFICULTY_TAG, this.difficulty.name());
    }
    if (this.type != null) {
      compoundTag.putString(DATA_QUEST_TYPE_TAG, this.type.name());
    }

    // Quest criterias and rewards.
    if (this.criterias != null) {
      ListTag criteriasTag = new ListTag();
      for (CriteriaData criteriaData : this.criterias) {
        CompoundTag criteriaTag = new CompoundTag();
        criteriaData.save(criteriaTag);
        criteriasTag.add(criteriaTag);
      }
      compoundTag.put(DATA_QUEST_CRITERIAS_TAG, criteriasTag);
    }
    if (this.rewards != null) {
      ListTag rewardsTag = new ListTag();
      for (RewardData rewardData : this.rewards) {
        CompoundTag rewardTag = new CompoundTag();
        rewardData.save(rewardTag);
        rewardsTag.add(rewardTag);
      }
      compoundTag.put(DATA_QUEST_REWARDS_TAG, rewardsTag);
    }

    // Quest Visuals
    if (this.icon != null) {
      compoundTag.put(DATA_QUEST_ICON_TAG, this.icon.save(new CompoundTag()));
    }
    if (this.background != null) {
      compoundTag.putString(DATA_QUEST_BACKGROUND_TAG, this.background.toString());
    }
    if (this.descriptionColor != 0) {
      compoundTag.putInt(DATA_QUEST_DESCRIPTION_COLOR_TAG, this.descriptionColor);
    }
    if (this.titleColor != 0) {
      compoundTag.putInt(DATA_QUEST_TITLE_COLOR_TAG, this.titleColor);
    }

    return compoundTag;
  }

  @Override
  public String toString() {
    return "QuestData [id=" + this.id + ", title=" + this.title + ", description="
        + this.description + ", category=" + this.category + ", difficulty=" + this.difficulty
        + ", type=" + this.type + ", criterias=" + this.criterias + ", rewards=" + this.rewards
        + ", icon=" + this.icon + ", background=" + this.background + ", descriptionColor="
        + this.descriptionColor + ", titleColor=" + this.titleColor + "]";
  }

}
