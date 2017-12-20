/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.eoinf.github.jiggen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.BorderTracer;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.FloodFiller;
import org.apache.logging.log4j.LogManager;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import org.apache.logging.log4j.Logger;

import java.io.File;

@State(Scope.Benchmark)
public class PixelSearcherBenchmark {
    private int width;
    private int height;
    private Pixmap pixmap;
    private Logger logger = LogManager.getLogger();

    @Setup
    public void prepare() {
        GdxNativesLoader.load();
        File f = new File("benchmark/src/main/resources/template.jpg");

        FileHandle handle = new FileHandle(f);
        this.pixmap = new Pixmap(handle);

        this.width = pixmap.getWidth();
        this.height = pixmap.getHeight();

        logger.info("Loaded template file: width = {}, height = {}", width, height);
    }


    @Benchmark
    public void floodFill(Blackhole bh) {
        FloodFiller pixelSearcher = new FloodFiller(pixmap, width, height);
        bh.consume(pixelSearcher.extractPiece(100, 50).isTraversed(105, 55));
    }

    @Benchmark
    public void borderTrace(Blackhole bh) {
        BorderTracer pixelSearcher = new BorderTracer(pixmap, width, height);
        bh.consume(pixelSearcher.extractPiece(100, 50).isTraversed(105, 55));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PixelSearcherBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
