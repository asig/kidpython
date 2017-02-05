/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public enum Key {
        Action_Run,
        Action_Resume,
        Action_Pause,
        Action_Stop,
        Action_Step_Into,
        Action_Step_Over,
        Action_About,
        Action_Preferences,
        Action_Help,

        Menu_File,
        MenuItem_Exit,

        StatusLine_Position,
        StatusLine_CAPS,
        StatusLine_NUM,
        StatusLine_SCROLL,

        VM_Execution_Started,
        VM_Execution_Stopped,
        VM_Execution_Paused,
        VM_Error_While_Compiling,

        Compiler_Token_FUNC,
        Compiler_Token_END,
        Compiler_Token_IF,
        Compiler_Token_THEN,
        Compiler_Token_ELSE,
        Compiler_Token_FOR,
        Compiler_Token_STEP,
        Compiler_Token_IN,
        Compiler_Token_TO,
        Compiler_Token_DO,
        Compiler_Token_WHILE,
        Compiler_Token_REPEAT,
        Compiler_Token_UNTIL,
        Compiler_Token_RETURN,
        Compiler_Token_AND,
        Compiler_Token_OR,
        Compiler_Token_PLUS,
        Compiler_Token_MINUS,
        Compiler_Token_ASTERISK,
        Compiler_Token_SLASH,
        Compiler_Token_LPAREN,
        Compiler_Token_RPAREN,
        Compiler_Token_LBRACK,
        Compiler_Token_RBRACK,
        Compiler_Token_LBRACE,
        Compiler_Token_RBRACE,
        Compiler_Token_COMMA,
        Compiler_Token_DOT,
        Compiler_Token_DOTDOT,
        Compiler_Token_COLON,
        Compiler_Token_EQ,
        Compiler_Token_NE,
        Compiler_Token_LE,
        Compiler_Token_LT,
        Compiler_Token_GE,
        Compiler_Token_GT,
        Compiler_Token_IDENT,
        Compiler_Token_STRING_LIT,
        Compiler_Token_NUM_LIT,
        Compiler_Token_EOT,
        Compiler_Token_UNKNOWN,

        Compiler_Error_Position,
        Compiler_Error_UNEXPECTED_TOKEN,
        Compiler_Error_RETURN_NOT_ALLOWED_OUTSIDE_FUNCTION,

        VarTable_Name,
        VarTable_Value,

        Toolbar_FollowCodeExecution,

        Preferences_CodeRepository,
        Preferences_CodeRepository_Connect,
        Preferences_CodeRepository_Disconnect,
        Preferences_CodeRepository_Connected,

        Preferences_ColorScheme,
        Preferences_ColorScheme_AddColorSchemes,
        Preferences_ColorScheme_AvailableColorSchemes,
        Preferences_ColorScheme_Preview
    }

    private static ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("com.programmablefun.Messages", Locale.GERMAN);
    }

    public static String get(Key key) {
        return bundle.getString(key.toString());
    }
}
