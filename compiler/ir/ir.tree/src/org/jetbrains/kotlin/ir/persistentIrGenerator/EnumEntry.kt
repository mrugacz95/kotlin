/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.persistentIrGenerator

internal fun PersistentIrGenerator.generateEnumEntry() {
    val correspondingClassField = Field("correspondingClass", IrClass + "?", "optional int64")
    val initializerExpressionField = Field("initializerExpression", IrExpressionBody + "?", "optional int32")

    writeFile("PersistentIrEnumEntry.kt", renderFile("org.jetbrains.kotlin.ir.declarations.persistent") {
        lines(
            id,
            +"internal class PersistentIrEnumEntry(",
            arrayOf(
                startOffset,
                endOffset,
                origin,
                +"override val symbol: " + irSymbol("IrEnumEntrySymbol"),
                name,
                irFactory,
            ).join(separator = ",\n").indent(),
            +") : " + baseClasses("EnumEntry") + " " + blockSpaced(
                initBlock,
                commonFields,
                descriptor(ClassDescriptor),

                correspondingClassField.toPersistentField(+"null"),
                initializerExpressionField.toBody()
            ),
            id,
        )()
    })

    writeFile("carriers/EnumEntryCarrier.kt", renderFile("org.jetbrains.kotlin.ir.declarations.persistent.carriers") {
        carriers(
            "EnumEntry",
            correspondingClassField,
            initializerExpressionField,
        )()
    })

    addCarrierProtoMessage("EnumEntry", correspondingClassField, initializerExpressionField)
}