package com.mineinabyss.protocolburrito.generation

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * Add this annotation to a class in another module to get codegen to actually run. Will be unnecessary once we switch
 * to KSP instead of kapt.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class RunGenerator

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@SupportedOptions(AnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnnotationProcessor(
    val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        var generatedDir: String = "build/generated/burrito/main/"
    }

//    override fun getSupportedAnnotationTypes() = mutableSetOf(RunGenerator::class.java.name)

//    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
//        generatedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]!!
//        main()
//        return false
//    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        main()
        return listOf()
    }
}

class ProtocolProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AnnotationProcessor(environment)
    }
}
