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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;

import de.markusbordihn.easyquests.Constants;

public class QuestDataManager {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  // Quest data folder path and file reference.
  public static final Path QUEST_DATA_FOLDER_PATH = Path.of(
      ServerLifecycleHooks.getCurrentServer().getWorldPath(LevelResource.ROOT).toFile().getPath(),
      Constants.MOD_ID).normalize();
  public static final File QUEST_DATA_FOLDER = QUEST_DATA_FOLDER_PATH.toFile();

  public static void saveQuestData(QuestData questData) {
    if (questData == null) {
      return;
    }

    // Quest resource location for storing the quest data.
    ResourceLocation resourceLocation = questData.getId();

    // Check if folder for namespace exists and create it if not.
    String namespace = resourceLocation.getNamespace();
    Path namespaceFolder = Paths.get(QUEST_DATA_FOLDER.toString(), namespace);
    try {
      Files.createDirectories(namespaceFolder);
    } catch (IOException e) {
      log.error("Error while creating quest folder for namespace: {}", namespace, e);
      return;
    }

    // Extract quest path folder/subfolder/... from quest resource path and check if folder exists.
    String questPath = resourceLocation.getPath();
    int indexQuestPath = questPath.lastIndexOf("/");
    if (indexQuestPath > 0) {
      String questPathFolder = questPath.substring(0, indexQuestPath);
      Path questPathFolderFile = namespaceFolder.resolve(questPathFolder);
      try {
        Files.createDirectories(questPathFolderFile);
      } catch (IOException e) {
        log.error("Error while creating quest folder for path: {}", questPathFolder, e);
        return;
      }
    }

    // Save quest data to compound tag.
    CompoundTag questDataTag = new CompoundTag();
    questData.save(questDataTag);

    // Save quest compound tag to nbt file.
    Path questDataFile = namespaceFolder.resolve(questPath + ".nbt");
    log.info("Save quest data to file: {}", questDataFile);
    try (OutputStream outputStream =
        new BufferedOutputStream(Files.newOutputStream(questDataFile))) {
      NbtIo.writeCompressed(questDataTag, outputStream);
    } catch (IOException e) {
      log.error("Error while saving quest data to file: {}", questDataFile, e);
    }
  }

  public static QuestData loadQuestData(Path filePath) {
    // Check if base quest path is included and normalize path.
    Path questPath = filePath.normalize();
    if (!questPath.startsWith(QUEST_DATA_FOLDER_PATH)) {
      questPath = QUEST_DATA_FOLDER_PATH.resolve(questPath);
    }

    // Check if quest file exists.
    File questFile = questPath.toFile();
    if (!questFile.exists()) {
      log.error("Quest file does not exist: {}", questFile);
      return null;
    }

    // Load quest data from nbt file.
    try (InputStream inputStream = new FileInputStream(questFile)) {
      CompoundTag compoundTag = NbtIo.readCompressed(inputStream);
      return QuestData.load(compoundTag);
    } catch (IOException exception) {
      log.error("Error while loading quest data from file: {}", questFile, exception);
      return null;
    }
  }

  public static List<Path> getQuestDataFiles() {
    List<Path> questDataFiles = new ArrayList<>();

    // Check if the quest data folder exists.
    if (!QUEST_DATA_FOLDER.exists()) {
      return questDataFiles;
    }

    try (Stream<Path> walk = Files.walk(QUEST_DATA_FOLDER.toPath())) {
      // Filter all NBT files and add them to the list.
      questDataFiles =
          walk.filter(p -> p.toFile().isFile() && p.getFileName().toString().endsWith(".nbt"))
              .map(QUEST_DATA_FOLDER_PATH::relativize).collect(Collectors.toList());
    } catch (IOException e) {
      log.error("Error while getting quest data files", e);
    }

    return questDataFiles;
  }
}
