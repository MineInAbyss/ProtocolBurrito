import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(ClassGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@ExperimentalStdlibApi
class ClassGenerator: AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private val elements: Elements by lazy { processingEnv.elementUtils }
    private val types: Types by lazy { processingEnv.typeUtils }
//    private val classInspector by lazy { ElementsClassInspector.create(elements, types) }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
    }
}
