package com.mineinabyss.protocolburrito.generation

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * TOP 10 HACKY CODE:
 *
 * #1 Trying to make decent codegen with pure gradle
 * #2 Making an annotation processor in kapt which literally just calls a function that then generates code and is
 *    included in the final build.
 *
 * I literally don't know how to do this correctly but this just works™ and every alternative with pure gradle seemed
 * worse...
 */
@Retention(AnnotationRetention.SOURCE)
annotation class RunGenerator

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnnotationProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        lateinit var generatedDir: String
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(RunGenerator::class.java.name)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        generatedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]!!
        main()
        return false
    }
}
