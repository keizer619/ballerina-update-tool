/*
 * Copyright (c) 2019, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballerinalang.command.cmd;

import org.ballerinalang.command.BallerinaCliCommands;
import org.ballerinalang.command.util.ErrorUtil;
import org.ballerinalang.command.util.ToolUtil;
import picocli.CommandLine;

import java.io.PrintStream;
import java.util.List;

/**
 * This class represents the "Update" command and it holds arguments and flags specified by the user.
 */
@CommandLine.Command(name = "use", description = "Use Ballerina distribution")
public class UseCommand extends Command implements BCommand {

    @CommandLine.Parameters(description = "Command name")
    private List<String> useCommands;

    @CommandLine.Option(names = {"--help", "-h", "?"}, hidden = true)
    private boolean helpFlag;

    private CommandLine parentCmdParser;

    public UseCommand(PrintStream printStream) {
        super(printStream);
    }

    public void execute() {
        if (helpFlag) {
            printUsageInfo(ToolUtil.CLI_HELP_FILE_PREFIX + BallerinaCliCommands.USE);
            return;
        }

        if (useCommands == null || useCommands.size() == 0) {
            throw ErrorUtil.createDistributionRequiredException("use");
        }

        if (useCommands.size() > 1) {
            throw ErrorUtil.createDistSubCommandUsageExceptionWithHelp("too many arguments", BallerinaCliCommands.USE);
        }

        PrintStream printStream = getPrintStream();
        String distribution = useCommands.get(0);
        if (distribution.equals(ToolUtil.getCurrentBallerinaVersion())) {
            printStream.println("'" + distribution + "' is the current active distribution version");
            return;
        }
        if (ToolUtil.checkDistributionAvailable(distribution)) {
            ToolUtil.useBallerinaVersion(printStream, distribution);
            printStream.println("'" + distribution + "' successfully set as the active distribution");
            return;
        }
        printStream.println("Distribution '" + distribution + "' not found");
        printStream.println("Run 'bal dist pull " + distribution + "' to fetch and set the distribution as the " +
                                    "active distribution");
    }

    @Override
    public String getName() {
        return BallerinaCliCommands.USE;
    }

    @Override
    public void printLongDesc(StringBuilder out) {

    }

    @Override
    public void printUsage(StringBuilder out) {
        out.append("  bal dist use\n");
    }

    @Override
    public void setParentCmdParser(CommandLine parentCmdParser) {
        this.parentCmdParser = parentCmdParser;
    }
}
