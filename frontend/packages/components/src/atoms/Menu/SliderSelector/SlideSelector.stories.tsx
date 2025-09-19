import React, { useState } from "react";
import type { Meta, StoryObj } from "@storybook/nextjs";
import { RangeSlider } from "./SlideSelector";

const meta: Meta<typeof RangeSlider> = {
    title: "Atoms/Menu/SliderSelector",
    component: RangeSlider,
};
export default meta;

type Story = StoryObj<typeof RangeSlider>;

export const Default: Story = {
    render: (args) => {
        const [range, setRange] = useState<[number, number]>([100, 500]);

        return (
            <RangeSlider
                {...args}
                selectedRange={range}
                onChange={setRange}
                header="Select Range"
            />
        );
    },
    args: {
        min: 0,
        max: 1000,
        step: 10,
        gap: 50,
    },
};