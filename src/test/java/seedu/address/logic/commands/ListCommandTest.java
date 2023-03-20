package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.commons.core.Messages.MESSAGE_LECTURE_DOES_NOT_EXIST;
import static seedu.address.commons.core.Messages.MESSAGE_MODULE_DOES_NOT_EXIST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_CODE_2103;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalModules.getTypicalTracker;

import static seedu.address.testutil.Assert.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ListCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lecture.LectureName;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.testutil.ModuleBuilder;
import seedu.address.testutil.TypicalModules;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    private ListCommand listCommand;
    private final Module module = new ModuleBuilder(TypicalModules.CS2040S).build();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTracker(), new UserPrefs());
        expectedModel = new ModelManager(model.getTracker(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS_MODULES, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_moduleFound_showsLectures() {
        ModuleCode moduleCode = module.getCode();
        String input = String.format("list /mod %s", moduleCode);
        try {
            listCommand = new ListCommandParser().parse(input);
            listCommand.execute(expectedModel);
        } catch (Exception e) {
            assertNull(e);
        }
        String expectedString = String.format(ListCommand.MESSAGE_SUCCESS_LECTURES, moduleCode);
        assertCommandSuccess(listCommand, model, expectedString, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_moduleFound_lectureFound_showsVideos() {
        ModuleCode moduleCode = module.getCode();
        LectureName lectureName = module.getLectureList().get(0).getName();
        String input = String.format("list /mod %s /lec %s", moduleCode, lectureName);
        try {
            listCommand = new ListCommandParser().parse(input);
            listCommand.execute(expectedModel);
        } catch (ParseException e) {
            assertNull(e);
        } catch (CommandException e) {
            assertNull(e);
        }
        String expectedString = String.format(ListCommand.MESSAGE_SUCCESS_VIDEOS, moduleCode, lectureName);
        assertCommandSuccess(listCommand, model, expectedString, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_moduleNotFound_throwsCommandException() {
        ModuleCode moduleCode = new ModuleCode(VALID_MODULE_CODE_2103);
        String input = String.format("list /mod %s", moduleCode);
        String expectedString = String.format(MESSAGE_MODULE_DOES_NOT_EXIST, moduleCode);
        try {
            listCommand = new ListCommandParser().parse(input);
            assertThrows(CommandException.class, expectedString,
                () -> listCommand.execute(expectedModel));
        } catch (ParseException e) {
            assertNull(e);
        }
    }

    @Test
    public void execute_listIsFiltered_lectureNotFound_throwsCommandException() {
        ModuleCode moduleCode = module.getCode();
        LectureName lectureName = new LectureName("Unknown lecture");
        String input = String.format("list /mod %s /lec %s", moduleCode, lectureName);
        String expectedString = String.format(MESSAGE_LECTURE_DOES_NOT_EXIST, lectureName, moduleCode);
        try {
            listCommand = new ListCommandParser().parse(input);
            assertThrows(CommandException.class, expectedString,
                () -> listCommand.execute(expectedModel));
        } catch (ParseException e) {
            assertNull(e);
        }
    }
}
