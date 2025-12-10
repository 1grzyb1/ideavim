# Architect Diary

This diary captures the reasoning behind the decisions I made while implementing the task requirements—what worked, what
didn’t, and what I’d do differently next time.

## Flip Command

Surprisingly, this part turned out to be quite straightforward. The only real difficulty was at the beginning, when I
was still completely unfamiliar with the codebase and had to orient myself.

Once past that, the implementation was simple: a single `FlipCommand` class that flips the words under each cursor.
That’s essentially the entire story for this feature.

## Autocmd

This feature caused the most headaches at first. Registering the `autocmd` command simply refused to work, and it took a
good amount of digging to discover that the issue stemmed from ANTLR itself. Once I understood that, everything else
fell into place.

At the core of the implementation is the `AutoCmdService`, which manages and executes all autocmd events. I chose to
represent events using `enums` so new event types can be added easily in the future. I’m aware this restricts
third-party plugins from defining their own events, but given the scope, I wasn’t sure whether plugin extensibility was
a goal. If we ever decide to support that, switching to constant strings would be the natural path forward.

Events are triggered by calling `handleEvent` in carefully selected locations throughout the code. I deliberately chose
this approach to ensure commands are always run in normal mode and behave predictably.

With this architecture in place, adding `BufEnter` and `BufLeave` was almost effortless.

Since this is an MVP, I allowed myself one shortcut: instead of fully relying on the autocmd grammar to parse arguments,
I just split them on `*`. In a real implementation, we should absolutely follow the grammar.

### augroup

Implementing `augroup` was also relatively smooth—ANTLR quirks aside. I cut another corner here and treated `augroup` as
a
standard command, even though a more accurate approach would include proper grammar support for nested autocmd blocks.
On the other hand, Vim itself allows arbitrary commands inside an `augroup`, so the exact boundaries of “proper” grammar
are debatable. Given the MVP scope, I kept it simple.

The logic mirrors Neovim’s behavior: when an `augroup` is active, commands are registered under that group; when we
encounter `augroup END`, the group context is cleared.

Unlike Neovim, which stores a numeric group ID for performance reasons, I just store the name. For an MVP, this is
perfectly acceptable—and it aligns with the “make it work → make it right → make it fast” philosophy.

## Command History

Ironically, this turned out to be the easiest feature—mainly because, by this point, I had become comfortable with the
codebase.

The only confusing part was figuring out why Vim wasn’t running inside the virtual file as expected. Once I understood
that, opening the virtual file using the IntelliJ API was straightforward.

There are definitely areas for improvement. For example, we currently rely on a rather fragile way of checking whether
an action is happening inside the command history window. The implementation is intentionally minimal: the window
doesn’t automatically close after executing a command, the filename probably shouldn’t be displayed, and multiple
history windows can be opened at once.

Running commands from the history window is also a bit naïve—it simply checks whether we’re in that window and then
executes the current line.

Still, despite the rough edges, it works—and that’s enough for an MVP.

At some point I had to force myself to stop polishing things. I was getting genuinely invested in all these features,
and I had to remind myself that these were just test tasks, not production-ready code. But it was fun to build, and I
learned a lot along the way.