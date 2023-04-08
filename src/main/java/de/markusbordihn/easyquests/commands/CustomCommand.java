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

package de.markusbordihn.easyquests.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import de.markusbordihn.easyquests.Constants;
import de.markusbordihn.easyquests.data.quest.QuestDataManager;
import de.markusbordihn.easyquests.data.quest.QuestManager;

public class CustomCommand {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  protected CustomCommand() {}

  protected static CompletableFuture<Suggestions> suggestQuestId(
      CommandContext<CommandSourceStack> context, SuggestionsBuilder build)
      throws CommandSyntaxException {
    ServerPlayer serverPlayer = context.getSource().getPlayerOrException();

    ArrayList<ResourceLocation> questIds = new ArrayList<>();
    QuestManager.getQuests().forEach(quest -> {
      questIds.add(quest.getId());
    });

    // Return all quests for creative mode and only the available of the player.
    return SharedSuggestionProvider.suggestResource(questIds, build);
  }

  protected static CompletableFuture<Suggestions> suggestQuestTitle(
      CommandContext<CommandSourceStack> context, SuggestionsBuilder build)
      throws CommandSyntaxException {
    ServerPlayer serverPlayer = context.getSource().getPlayerOrException();

    ArrayList<String> questTitles = new ArrayList<>();
    QuestManager.getQuests().forEach(quest -> {
      questTitles.add('"' + quest.getTitle() + '"');
    });

    // Return all quests for creative mode and only the available of the player.
    return SharedSuggestionProvider.suggest(questTitles, build);
  }

  protected static CompletableFuture<Suggestions> suggestQuestFile(
      CommandContext<CommandSourceStack> context, SuggestionsBuilder build) {

    // Format quest files for suggestion.
    List<Path> questFiles = QuestDataManager.getQuestDataFiles();
    ArrayList<String> questFilePaths = new ArrayList<>();
    questFiles.forEach(questPath -> {
      log.info(questPath);
      questFilePaths.add('"' + questPath.toString().replace("\\","/") + '"');
    });

    // Return all quests for creative mode and only the available of the player.
    return SharedSuggestionProvider.suggest(questFilePaths, build);
  }

}
