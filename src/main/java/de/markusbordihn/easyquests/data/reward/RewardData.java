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

package de.markusbordihn.easyquests.data.reward;

import net.minecraft.commands.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class RewardData {

  public static final RewardData EMPTY = new RewardData(RewardType.CUSTOM);

  private RewardType rewardType;
  private final int experience;
  private final ResourceLocation[] loot;
  private final ResourceLocation[] recipes;
  private final CommandFunction.CacheableFunction function;

  public RewardData(RewardType rewardType) {
    this.rewardType = rewardType;
    this.experience = 0;
    this.loot = new ResourceLocation[0];
    this.recipes = new ResourceLocation[0];
    this.function = null;
  }

  public RewardType getType() {
    return this.rewardType;
  }

  public void setRewardType(RewardType rewardType) {
    this.rewardType = rewardType;
  }

  public static RewardData load(CompoundTag compoundTag) {
    return null;
  }

  public void save(CompoundTag compoundTag) {}
}
