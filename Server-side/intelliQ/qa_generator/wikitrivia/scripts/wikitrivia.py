from wikitrivia.article import Article

import click
import json
import sys


def get_file_content(file_name):
    with open(file_name, 'r') as stream:
        content = stream.read()

    return content


def generate_trivia(titles):
    """ Generates trivia questions provided text. 
        @titles: dictionary of {header: text}
    """
    # Use the sample articles if the user didn't supply any
    if not titles:
        sys.exit("No topics fed!")

    # Retrieve the trivia sentences
    questions = []
    for article in titles:
        text = titles[article]
        click.echo('Analyzing \'{0}\''.format(article))
        if text is None:
            text = get_file_content(article)
        article = Article(title=article, text=text)
        questions = questions + article.generate_trivia_sentences()

    result = json.dumps(questions, sort_keys=True, indent=4)
    print(result)
    return result


@click.command()
@click.argument('titles', nargs=-1)
def qa_generator(titles):
    title_dict = {}
    for title in titles:
        title_dict[title] = None
    generate_trivia(title_dict)


if __name__ == '__main__':
    generate_trivia(sys.argv)
