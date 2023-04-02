package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ARCHIVE_FILE_NAME;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Tracker;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.TypicalModules;


public class ExportCommandTest {
    @TempDir
    public Path testFolder;
    private Model model = new ModelStubWithTracker();

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ExportCommand command = new ExportCommand(VALID_ARCHIVE_FILE_NAME, false);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        ExportCommand exportCommand = new ExportCommand(VALID_ARCHIVE_FILE_NAME, false);

        ExportCommand exportCommandCopy = new ExportCommand(VALID_ARCHIVE_FILE_NAME, false);
        ExportCommand exportCommandOverwrite = new ExportCommand(VALID_ARCHIVE_FILE_NAME, true);

        assertTrue(exportCommand.equals(exportCommand));
        assertTrue(exportCommand.equals(exportCommandCopy));
        assertFalse(exportCommand.equals(1));
        assertFalse(exportCommand.equals(exportCommandOverwrite));
    }

    @Test
    public void execute_correctCommand_returnCommandResult() throws CommandException {
        Path savePath = testFolder.resolve(VALID_ARCHIVE_FILE_NAME);
        ExportCommand exportCommand = new ExportCommand(VALID_ARCHIVE_FILE_NAME, false);
        CommandResult expectedCommandResult = new CommandResult(String.format(MESSAGE_SUCCESS,
                VALID_ARCHIVE_FILE_NAME), savePath, true, false);
        assertEquals(expectedCommandResult, exportCommand.execute(model));
        assertEquals(expectedCommandResult.getPath().get(), savePath);
    }



    /**
     * A {@code Model} stub that contains a tracker.
     */

    private class ModelStubWithTracker extends ModelStub {
        private Tracker tracker;

        public ModelStubWithTracker() {
            this.tracker = TypicalModules.getTypicalTracker();
        }

        @Override
        public Tracker getTracker() {
            return tracker;
        }
    }

    /*

    private class StorageStubForExport extends StorageStub {
        private final Path archivePath;
        public StorageStubForExport(Path archivePath) {
            this.archivePath = archivePath;
        }

        public void saveTracker(ReadOnlyTracker tracker, Path filePath) {

            try {
                new JsonTrackerStorage(archivePath)
                        .saveTracker(tracker, archivePath);
            } catch (IOException ioe) {
                throw new AssertionError("There should not be an error writing to the file.", ioe);
            }
        }

        public Optional<ReadOnlyTracker> readTracker() {
            return Optional.of(TypicalModules.getTypicalTracker());
        }
    }
    */
}

