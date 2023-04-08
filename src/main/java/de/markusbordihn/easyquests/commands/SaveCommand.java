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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import de.markusbordihn.easyquests.data.quest.QuestData;
import de.markusbordihn.easyquests.data.quest.QuestDataManager;
import de.markusbordihn.easyquests.data.quest.QuestManager;

public class SaveCommand extends CustomCommand {

  public static final String ID_ARG = "id";
  public static final String TITLE_ARG = "title";

  public static ArgumentBuilder<CommandSourceStack, ?> register() {

    return Commands.literal("save")
        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))

        // Show quest by title.
        .then(Commands.argument(TITLE_ARG, StringArgumentType.string()).executes(SaveCommand::save))

        // Show quest by id.
        .then(Commands.argument(ID_ARG, ResourceLocationArgument.id())
            .suggests(SaveCommand::suggestQuestId).executes(SaveCommand::saveById));
  }

  private static int save(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    CommandSourceStack source = context.getSource();

    // Validate title and show error message if title is invalid.
    String title = StringArgumentType.getString(context, TITLE_ARG);
    if (title == null || title.isEmpty()) {
      source.sendFailure(new TextComponent("Title " + title + " is not valid!"));
      return 0;
    }

    // Get quest data and show error message if quest is not found.
    QuestData questData = QuestManager.getQuest(title);
    if (questData == null) {
      source.sendFailure(new TextComponent("Quest with title " + title + " not found!"));
      return 0;
    }

    source.sendSuccess(new TextComponent("Save quest with title: " + title + ":" + questData),
        false);
    QuestDataManager.saveQuestData(questData);
    return Command.SINGLE_SUCCESS;
  }

  private static int saveById(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    CommandSourceStack source = context.getSource();

    // Verify resource id and show error message if id is invalid.
    ResourceLocation resourceLocation = ResourceLocationArgument.getId(context, ID_ARG);
    if (resourceLocation == null) {
      source.sendFailure(new TextComponent("Resource id " + resourceLocation + " is not valid!"));
      return 0;
    }

    // Get quest data and show error message if quest is not found.
    QuestData questData = QuestManager.getQuest(resourceLocation);
    if (questData == null) {
      source.sendFailure(new TextComponent("Quest with id " + resourceLocation + " not found!"));
      return 0;
    }

    source.sendSuccess(
        new TextComponent("Save quest with id " + resourceLocation + ":" + questData), false);
    QuestDataManager.saveQuestData(questData);
    return Command.SINGLE_SUCCESS;
  }

}
