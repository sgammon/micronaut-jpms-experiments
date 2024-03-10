import org.example.api.ModularResources;

@ModularResources("org.sample.lib.ResourceProvider") open module demo.libmodule {
  requires static demo.annotations;
  requires java.base;

  exports org.sample.lib;
}
