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

import java.nio.file.Paths;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

import de.markusbordihn.easyquests.data.quest.QuestData;
import de.markusbordihn.easyquests.data.quest.QuestDataManager;

public class LoadCommand extends CustomCommand {

  public static final String FILENAME_ARG = "filename";

  public static ArgumentBuilder<CommandSourceStack, ?> register() {

    return Commands.literal("load")
        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))

        // Show quest by filename.
        .then(Commands.argument(FILENAME_ARG, StringArgumentType.string())
            .suggests(LoadCommand::suggestQuestFile).executes(LoadCommand::load));
  }

  private static int load(CommandContext<CommandSourceStack> context) {
    CommandSourceStack source = context.getSource();

    // Validate title and show error message if title is invalid.
    String filename = StringArgumentType.getString(context, FILENAME_ARG);
    if (filename == null || filename.isEmpty()) {
      source.sendFailure(new TextComponent("Filename " + filename + " is not valid!"));
      return 0;
    }

    // Load quest by filename.
    QuestData questData = QuestDataManager.loadQuestData(Paths.get(filename));
    if (questData == null) {
      source.sendFailure(new TextComponent("Quest " + filename + " not found!"));
      return 0;
    }

    source.sendSuccess(new TextComponent("Quest " + filename + " loaded!" + questData), true);
    return Command.SINGLE_SUCCESS;
  }

}
