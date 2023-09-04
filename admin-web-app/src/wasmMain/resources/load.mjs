import { instantiate } from './devicecom.uninstantiated.mjs';

await wasmSetup;
instantiate({ skia: Module['asm'] });