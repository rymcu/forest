/**
 * IK 中文分词  版本 5.0.1
 * IK Analyzer release 5.0.1
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 */
package com.rymcu.forest.lucene.lucene;

import com.rymcu.forest.lucene.core.IKSegmenter;
import com.rymcu.forest.lucene.core.Lexeme;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;


/**
 * IK分词器 Lucene Tokenizer适配器类
 * 兼容Lucene 6.0版本
 */
public final class IKTokenizer extends Tokenizer {
    // IK分词器实现
    private IKSegmenter _IKImplement;

    // 词元文本属性
    private final CharTermAttribute termAtt;
    // 词元位移属性
    private final OffsetAttribute offsetAtt;
    // 词元分类属性（该属性分类参考org.wltea.analyzer.core.Lexeme中的分类常量）
    private final TypeAttribute typeAtt;
    // 记录最后一个词元的结束位置
    private int endPosition;

    private Tokenizer delegate;

    /**
     * Lucene 6.0 Tokenizer适配器类构造函数
     *
     * @param useSmart
     */
    public IKTokenizer(boolean useSmart) {
        super();
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        //传入 IKSegmenter 的 input Reader  流，会被 父类 Tokenizer 类的无参构造器
        //初始化为 this.input = ILLEGAL_STATE_READER;
        _IKImplement = new IKSegmenter(input, useSmart);
    }

    /**
     * lucene 6.0 新增
     * 方便创建 工厂类
     *
     * @param factory
     * @param useSmart
     */
    public IKTokenizer(AttributeFactory factory, boolean useSmart) {
        super(factory);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, useSmart);
    }


    @Override
    public boolean incrementToken() throws IOException {
        // 清除所有的词元属性
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if (nextLexeme != null) {
            // 将Lexeme转成Attributes
            // 设置词元文本
            termAtt.append(nextLexeme.getLexemeText());
            // 设置词元长度
            termAtt.setLength(nextLexeme.getLength());
            // 设置词元位移
            offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            // 记录分词的最后位置
            endPosition = nextLexeme.getEndPosition();
            // 记录词元分类
            typeAtt.setType(nextLexeme.getLexemeTypeString());
            // 返会true告知还有下个词元
            return true;
        }
        // 返会false告知词元输出完毕
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }

    @Override
    public final void end() {
        // set final offset
        int finalOffset = correctOffset(this.endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
    }
}
