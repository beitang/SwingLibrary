/*
 * Copyright 2008-2011 Nokia Siemens Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.swing.keyword.window;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.swing.context.AbstractContextVerifier;
import org.robotframework.swing.context.Context;
import org.robotframework.swing.factory.IdentifierParsingOperatorFactory;
import org.robotframework.swing.window.FrameOperator;
import org.robotframework.swing.window.FrameOperatorFactory;

import abbot.tester.WindowTester;

@RobotKeywords
public class WindowKeywords extends AbstractContextVerifier {
    private final IdentifierParsingOperatorFactory<FrameOperator> operatorFactory = new FrameOperatorFactory();

    public WindowKeywords() {
        super(
                "To use this keyword you must first select a window as context using the 'Select Window'-keyword.");
    }

    @RobotKeyword("Selects the window that was opened first as current context.\n\n"
            + "Example:\n" + "| Select Main Window |\n")
    public void selectMainWindow() {
        setContext(operatorFactory.createOperatorByIndex(0));
    }

    @RobotKeyword("Gets list of open window titles.\n\n"
            +"Logs the window titles and names in parenthesis.\n"
            + "Example:\n" + "| List Windows |\n")
    public List<String> listWindows() {
        List<String> result = new ArrayList<String>();
        for (Frame frame: Frame.getFrames()) {
            result.add(frame.getTitle());
            System.out.println( frame.getTitle() + " (" + frame.getName() +")");
        }
        return result;
    }

    @RobotKeyword("Selects a window as current context and sets focus to it.\n\n"
            + "*N.B.* Regular expression can be used to select the window by prefixing the identifier with 'regexp='.\n"
            + "Please learn more about java reqular expressions at http://java.sun.com/docs/books/tutorial/essential/regex/ \n "
            + "and patterns http://java.sun.com/javase/7/docs/api/java/util/regex/Pattern.html \n\n"
            + "Example:\n"
            + "| Select Window | _Help_ |\n"
            + "| Select Window | _regexp=^H.*_ | Selects a window starting with letter H. |\n")
    @ArgumentNames({ "identifier" })
    public void selectWindow(String identifier) {
        FrameOperator operator = operatorFactory.createOperator(identifier);
        setContext(operator);
    }

    @RobotKeyword("Closes a window.\n\n"
            + "*N.B.* Regular expression can be used to close the window by prefixing the identifier with 'regexp='.\n"
            + "Please learn more about java reqular expressions at http://java.sun.com/docs/books/tutorial/essential/regex/ \n "
            + "and patterns http://java.sun.com/javase/7/docs/api/java/util/regex/Pattern.html \n\n"
            + "Example:\n"
            + "| Close Window | _Help_ |\n"
            + "| Close Window | _regexp=^H.*_ | Closes a window starting with letter H. |\n")
    @ArgumentNames({ "identifier" })
    public void closeWindow(String identifier) {
        FrameOperator operator = operatorFactory.createOperator(identifier);
        operator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        new WindowTester().actionClose(operator.getSource());
    }

    @RobotKeyword("Returns the title of the selected window.\n"
            + "Assumes current context is window.\n\n" + "Example:\n"
            + "| ${title}=     | Get Selected Window Title |            |\n"
            + "| Should Be Equal | _Help Contents_           | _${title}_ |\n")
    public String getSelectedWindowTitle() {
        return frameOperator().getTitle();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Component>[] getExpectedClasses() {
        return new Class[] { Window.class };
    }

    private void setContext(FrameOperator frameOperator) {
        frameOperator.getFocus();
        Context.setContext(frameOperator);
    }

    private FrameOperator frameOperator() {
        verifyContext();
        return (FrameOperator) Context.getContext();
    }
}
