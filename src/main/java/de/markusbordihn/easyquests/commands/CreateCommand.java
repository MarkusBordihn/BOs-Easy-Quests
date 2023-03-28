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

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import de.markusbordihn.easyquests.Constants;
import de.markusbordihn.easyquests.data.quest.QuestData;
import de.markusbordihn.easyquests.data.quest.QuestManager;

public class CreateCommand extends CustomCommand {

  public static final String DESCRIPTION_ARG = "description";
  public static final String ID_ARG = "id";
  public static final String TITLE_ARG = "title";

  public static ArgumentBuilder<CommandSourceStack, ?> register() {
    return Commands.literal("create")
        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
        .executes(CreateCommand::usage)

        // Create quest with title.
        .then(Commands.argument(TITLE_ARG, StringArgumentType.string())
            .executes(CreateCommand::create))

        // Create quest with title and description.
        .then(Commands.argument(TITLE_ARG, StringArgumentType.string())
            .then(Commands.argument(DESCRIPTION_ARG, StringArgumentType.string())
                .executes(CreateCommand::create)))

        // Create quest with id and title.
        .then(Commands.argument(ID_ARG, ResourceLocationArgument.id())
            .then(Commands.argument(TITLE_ARG, StringArgumentType.string())
                .executes(CreateCommand::createWithID)))

        // Create quest with id, title and description.
        .then(Commands.argument(ID_ARG, ResourceLocationArgument.id())
            .then(Commands.argument(TITLE_ARG, StringArgumentType.string())
                .then(Commands.argument(DESCRIPTION_ARG, StringArgumentType.string())
                    .executes(CreateCommand::createWithID))));
  }

  private static int usage(CommandContext<CommandSourceStack> context) {
    CommandSourceStack source = context.getSource();
    source.sendSuccess(new TextComponent("Usage:\n" + "/" + Constants.MOD_ID + " create <title>\n"
        + "/" + Constants.MOD_ID + " create <title> <description>\n" + "/" + Constants.MOD_ID
        + " create <id> <title>\n" + "/" + Constants.MOD_ID + " create <id> <title> <description>"),
        false);

    return Command.SINGLE_SUCCESS;
  }

  private static int create(CommandContext<CommandSourceStack> context) {
    CommandSourceStack source = context.getSource();

    // Handle title and verify quest id and show error message if quest id is already in use.
    String title = StringArgumentType.getString(context, TITLE_ARG);
    if (QuestManager.hasQuest(title)) {
      ResourceLocation resourceLocation = QuestData.getQuestId(title);
      source.sendFailure(new TextComponent("Quest id " + resourceLocation + " is already in use!"));
      return 0;
    }

    // Handle optional description.
    String description;
    try {
      description = StringArgumentType.getString(context, DESCRIPTION_ARG);
    } catch (IllegalArgumentException e) {
      description = null;
    }

    // Create quest and show success message.
    if (description != null && !description.isEmpty()) {
      QuestData questData = QuestManager.createQuest(title, description);
      source.sendSuccess(new TextComponent("Create quest: " + questData), false);
      return Command.SINGLE_SUCCESS;
    } else {
      QuestData questData = QuestManager.createQuest(title);
      source.sendSuccess(new TextComponent("Create quest: " + questData), false);
      return Command.SINGLE_SUCCESS;
    }
  }

  private static int createWithID(CommandContext<CommandSourceStack> context) {
    CommandSourceStack source = context.getSource();

    // Verify resource id and show error message if id is already in use.
    ResourceLocation resourceLocation = ResourceLocationArgument.getId(context, ID_ARG);
    if (resourceLocation == null) {
      source.sendFailure(new TextComponent("Resource id " + resourceLocation + " is not valid!"));
      return 0;
    }

    // Minecraft namespace is not allowed, so we need to change it to our own namespace.
    if ("minecraft".equals(resourceLocation.getNamespace())) {
      resourceLocation = new ResourceLocation(Constants.QUEST_NAMESPACE,
          resourceLocation.getPath().contains("/") ? resourceLocation.getPath()
              : "quests/" + resourceLocation.getPath());
    }

    // Verify resource id and show error message if quest id is already in use.
    if (QuestManager.hasQuest(resourceLocation)) {
      source.sendFailure(new TextComponent("Quest id " + resourceLocation + " is already in use!"));
      return 0;
    }

    // Handle title.
    String title = StringArgumentType.getString(context, TITLE_ARG);

    // Handle optional description.
    String description;
    try {
      description = StringArgumentType.getString(context, DESCRIPTION_ARG);
    } catch (IllegalArgumentException e) {
      description = null;
    }

    // Create quest and show success message.
    if (description != null && !description.isEmpty()) {
      QuestData questData = QuestManager.createQuest(resourceLocation, title, description);
      source.sendSuccess(new TextComponent("Create quest " + resourceLocation + " : " + questData),
          false);
      return Command.SINGLE_SUCCESS;
    } else {
      QuestData questData = QuestManager.createQuest(resourceLocation, title);
      source.sendSuccess(new TextComponent("Create quest " + resourceLocation + " : " + questData),
          false);
      return Command.SINGLE_SUCCESS;
    }
  }

}
